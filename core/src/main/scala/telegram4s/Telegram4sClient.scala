package telegram4s

import java.util.UUID

import cats.effect.{Blocker, ConcurrentEffect, ContextShift, Resource}
import cats.implicits._
import com.typesafe.scalalogging.StrictLogging
import io.circe.parser.parse
import io.circe.{Decoder, Encoder}
import fs2._
import org.http4s.Uri.{Authority, RegName, Scheme}
import org.http4s._
import org.http4s.circe.{accumulatingJsonOf, jsonEncoderWithPrinterOf}
import org.http4s.client.Client
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.multipart.{Multipart, Part}
import telegram4s.Telegram4sClient.TelegramHost
import telegram4s.marshalling._
import telegram4s.marshalling.codecs._
import telegram4s.methods.TelegramMethod
import telegram4s.models.{InputFile, TelegramResponse}

import scala.concurrent.ExecutionContext

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
  implicit def entityEncoder[E: Encoder]: EntityEncoder[F, E] = jsonEncoderWithPrinterOf[F, E](printer)
  val apiBaseUrl = s"/bot$token/"

  def execute[Req, Res](request: Req)(implicit method: TelegramMethod[Req, Res]): F[Res] = {
    val uuid = UUID.randomUUID()
    logger.debug(s"REQUEST $uuid $request")

    CE.attempt(sendRequest[Req, Res](request, method))
      .flatTap {
        case Right(response) => logger.debug(s"RESPONSE $uuid $response").pure[F]
        case Left(e)         => logger.error(s"RESPONSE $uuid $e").pure[F]
      }
      .rethrow
  }

  def sendRequest[Req, Res](apiRequest: Req, method: TelegramMethod[Req, Res]): F[Res] = {
    import method._

    client
      .fetchAs[TelegramResponse[Res]](createRequest(apiRequest, method))
      .flatMap(handleResponse)
  }

  def createRequest[Req, Res](request: Req, method: TelegramMethod[Req, Res]): Request[F] = {
    val uri = Uri(
      path = apiBaseUrl + method.name,
      authority = Authority(host = RegName(TelegramHost)).some,
      scheme = Scheme.https.some
    )

    val uploads = method.attachments(request).map {
      case (camelKey, inputFile) =>
        val key = camelKey.snakeCase
        inputFile match {
          case InputFile.Existing(id)               =>
            Part.formData[F](key, id)
          case InputFile.Upload(filename, contents) =>
            Part.fileData[F](key, filename, Stream.emits[F, Byte](contents))
          case InputFile.Path(path)                 =>
            val blocker = Blocker.liftExecutionContext(ec)
            Part.fileData[F](key, path.toFile.getName, io.file.readAll[F](path, blocker, 2048))
        }
    }

    if (uploads.isEmpty) jsonRequest(uri, request, method)
    else multipartRequest(uri, request, method, uploads)
  }

  private def jsonRequest[Req, Res](uri: Uri, req: Req, method: TelegramMethod[Req, Res]): Request[F] = {
    import method.encoder
    Request[F](Method.POST, uri).withEntity(req)
  }

  private def multipartRequest[Req, Res](uri: Uri,
                                         request: Req,
                                         method: TelegramMethod[Req, Res],
                                         parts: List[Part[F]]): Request[F] = {
    val fields: Map[String, String] = parse(method.encoder(request).printWith(printer))
      .toOption
      .flatMap(_.asObject)
      .map(_.toMap.view.mapValues(printer.print).toMap)
      .getOrElse(Map())

    val paramParts: Vector[Part[F]] = fields.map { case (key, value) => Part.formData[F](key, value) }.toVector

    Request[F](Method.POST, uri).withEntity(Multipart[F](parts.toVector ++ paramParts))
  }

  def handleResponse[E](response: TelegramResponse[E]): F[E] = response match {
    case TelegramResponse(true, Some(result), _, _, _) =>
      result.pure[F]

    case TelegramResponse(false, _, description, Some(errorCode), parameters) =>
      TelegramApiException(description.getOrElse("Unexpected/invalid/empty response"), errorCode, None, parameters).raiseError[F, E]

    case other =>
      Telegram4sException(s"Unexpected API response: $other").raiseError[F, E]
  }
}
