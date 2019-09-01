package telegram4s.syntax

import telegram4s.Telegram4sClient
import telegram4s.methods.TelegramMethod

import scala.language.higherKinds

final class TelegramMethodOps[A](private val a: A) extends AnyVal {
  def call[F[_], R](implicit client: Telegram4sClient[F], method: TelegramMethod[A, R]): F[R] = client.execute(a)
}