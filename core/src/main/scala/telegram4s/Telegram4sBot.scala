package telegram4s

import cats.effect.{ConcurrentEffect, Resource, Timer}
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Queue
import org.http4s._
import org.http4s.circe.jsonOf
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder
import telegram4s.methods.updates.GetUpdates
import telegram4s.methods.webhooks.{DeleteWebhook, SetWebhook}
import telegram4s.models.{InputFile, Update}
import telegram4s.syntax._

object Telegram4sBot {
  def poll[F[_] : ConcurrentEffect : Telegram4sClient](initialOffset: Long = 0): Stream[F, Update] = {
    def pollStream(initialOffset: Long): Stream[F, Update] = {
      Stream(())
        .repeat
        .covary[F]
        .evalMapAccumulate(initialOffset) { case (offset, _) =>
          GetUpdates(offset.some, timeout = 30.some).call
            .map(updates => (latestOffset(updates, offset), updates))
            .recover { case _ => offset -> List.empty }
        }
        .flatMap {
          case (_, updates) => Stream.emits(updates)
        }
    }

    def latestOffset(updates: Seq[Update], current: Long): Long = {
      updates.map(_.updateId).maxOption.map(_ + 1).getOrElse(current)
    }

    Stream.eval(DeleteWebhook.call) >> pollStream(initialOffset)
  }

  def hook[F[_] : ConcurrentEffect : Timer : Telegram4sClient](url: String,
                                                               port: Int,
                                                               certificate: Option[InputFile]): Resource[F, Stream[F, Update]] = {
    def setHookResource(): Resource[F, Unit] =
      Resource.make(SetWebhook(url, certificate).call.void) { _ =>
        DeleteWebhook.call.void
      }

    def serverResource: Resource[F, Stream[F, Update]] = {
      val dsl = Http4sDsl[F]
      import dsl._

      def app(queue: Queue[F, Update]): HttpApp[F] =
        HttpRoutes
          .of[F] {
            case req@POST -> Root =>
              req
                .decodeWith(jsonOf[F, Update], strict = true)(queue.enqueue1(_) *> Ok())
                .recoverWith {
                  case InvalidMessageBodyFailure(_, _) => Ok()
                }
          }
          .orNotFound

      def server(queue: Queue[F, Update]): Resource[F, Server[F]] =
        BlazeServerBuilder[F].bindHttp(port).withHttpApp(app(queue)).resource

      Resource.suspend(Queue.unbounded[F, Update].map(q => server(q).map(_ => q.dequeue)))
    }

    for {
      _ <- setHookResource()
      updates <- serverResource
    } yield updates
  }
}

