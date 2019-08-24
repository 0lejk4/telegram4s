package telegram4s

import java.util.UUID

import cats.effect.{ConcurrentEffect, ContextShift, Resource}
import cats.implicits._
import com.typesafe.scalalogging.StrictLogging
import io.circe.parser.parse
import io.circe.syntax._
import io.circe.{Decoder, Encoder}
import fs2._
import org.http4s.Uri.{Authority, RegName, Scheme}
import org.http4s.circe.{accumulatingJsonOf, jsonEncoderOf}
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.multipart.{Multipart, Part}
import org.http4s.{EntityDecoder, EntityEncoder, Headers, Method, Request, Uri}
import telegram4s.Telegram4sClient.TelegramHost
import telegram4s.marshalling._
import telegram4s.methods.{ApiRequest, JsonApiRequest, MultipartApiRequest, TelegramResponse}
import telegram4s.models.InputFile

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

object Telegram4sClient {
  val TelegramHost = "api.telegram.org"

  def apply[F[_]](token: String)
                 (implicit CE: ConcurrentEffect[F], CF: ContextShift[F], ec: ExecutionContext): Resource[F, Telegram4sClient[F]] = {
    BlazeClientBuilder[F](ec).resource.map(new Telegram4sClient[F](_, token))
  }
}

class Telegram4sClient[F[_]](client: Client[F],
                             token: String)
                            (implicit CE: ConcurrentEffect[F], CF: ContextShift[F], ec: ExecutionContext) extends StrictLogging {
  implicit def entityDecoder[E: Decoder]: EntityDecoder[F, E] = accumulatingJsonOf[F, E]
  implicit def entityEncoder[E: Encoder]: EntityEncoder[F, E] = jsonEncoderOf[F, E]
  val apiBaseUrl = s"/bot$token/"

  def apply(request: ApiRequest): F[request.Response] = {
    val uuid = UUID.randomUUID()
    logger.trace(s"REQUEST $uuid $request")

    CE.attempt(sendRequest(request))
      .flatTap {
        case Right(response) => logger.trace(s"RESPONSE $uuid $response").pure[F]
        case Left(e)         => logger.error(s"RESPONSE $uuid $e").pure[F]
      }
      .rethrow
  }

  def sendRequest(apiRequest: ApiRequest): F[apiRequest.Response] = {
    import apiRequest.responseDecoder

    client
      .fetchAs[TelegramResponse[apiRequest.Response]](apiRequest2HttpRequest(apiRequest))
      .flatMap(handleResponse)
  }

  def apiRequest2HttpRequest(request: ApiRequest): Request[F] = {
    val uri = Uri(
      path = apiBaseUrl + request.methodName,
      authority = Authority(host = RegName(TelegramHost)).some,
      scheme = Scheme.https.some
    )

    request match {
      case _: JsonApiRequest[_] =>
        Request[F](Method.POST, uri, headers = Headers()).withEntity(request)

      case r: MultipartApiRequest[_] =>
        val files = r.getFiles

        val parts: Vector[Part[F]] = files.map {
          case (camelKey, inputFile) =>
            val key = StringUtils.snakenize(camelKey)
            inputFile match {
              case InputFile.FileId(id)                   => Part.formData(key, id)
              case InputFile.Contents(filename, contents) => Part.fileData(key, filename, Stream.emits[F, Byte](contents))
              case InputFile.Path(path)                   => Part.fileData(key, path.toFile.getName, io.file.readAll[F](path, ec, 2048))
            }
        }.toVector

        val fields = parse(request.asJson.pretty(printer))
          .map(_.asObject)
          .fold(_ => None, identity)
          .map(_.toMap.mapValues(printer.pretty))
          .getOrElse(Map())

        val paramParts: Vector[Part[F]] = fields.map { case (key, value) => Part.formData(key, value) }.toVector

        Request[F](Method.POST, uri).withEntity(Multipart[F](parts ++ paramParts))
    }
  }

  def handleResponse[E](response: TelegramResponse[E]): F[E] = response match {
    case TelegramResponse(true, Some(result), _, _, _) =>
      result.pure[F]

    case TelegramResponse(false, _, description, Some(errorCode), parameters) =>
      Telegram4sException(description.getOrElse("Unexpected/invalid/empty response"), errorCode, None, parameters).raiseError[F, E]

    case other =>
      new RuntimeException(s"Unexpected API response: $other").raiseError[F, E]
  }
}
