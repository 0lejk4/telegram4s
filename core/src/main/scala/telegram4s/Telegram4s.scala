package telegram4s

;

import cats.effect.ConcurrentEffect
import cats.implicits._
import fs2.Stream
import telegram4s.methods._
import telegram4s.models.Update

import scala.collection.mutable.ListBuffer
import scala.language.higherKinds

case class Telegram4s[F[_] : ConcurrentEffect](client: Telegram4sClient[F]) {
  var handlers: ListBuffer[Update => F[Unit]] = ListBuffer.empty

  def onUpdate(f: Update => Unit): Telegram4s[F] = onUpdateF(f(_).pure[F])

  def onUpdateF(f: Update => F[Unit]): Telegram4s[F] = {
    handlers += f
    this
  }

  def poll(initialOffset: Long = 0): F[Unit] = {
    client(DeleteWebhook).ifM(
      pollStream(initialOffset)
        .flatMap(u => handlers.map(f => Stream.eval(f(u))).reduce(_ ++ _))
        .compile
        .drain,
      new Exception("Can not remove webhook").raiseError[F, Unit])
  }

  def pollStream(initialOffset: Long): Stream[F, Update] = {
    Stream(())
      .repeat
      .covary[F]
      .evalMapAccumulate(initialOffset) { case (offset, _) =>
        client(GetUpdates((offset + 1).some, timeout = 1.some))
          .map(updates => (latestOffset(updates, offset), updates))
          .recover { case _ => offset -> Seq.empty }
      }
      .flatMap {
        case (_, updates) => Stream.emits(updates)
      }
  }

  def latestOffset(updates: Seq[Update], current: Long): Long = {
    updates.map(_.updateId).sorted.headOption.getOrElse(current)
  }
}

