package telegram4s.models.messages

import telegram4s.models.Chat
import cats.syntax.functor._
import io.circe.Decoder

trait TelegramMessage {
  def messageId: Int
  def chat: Chat
  def date: Int
}

object TelegramMessage {

  implicit val telegramMessageDecoder: Decoder[TelegramMessage] =
    List[Decoder[TelegramMessage]](
      UserMessage.userMessageDecoder.widen,
      SystemMessage.systemMessageDecoder.widen
    ).reduceLeft(_.or(_))
}
