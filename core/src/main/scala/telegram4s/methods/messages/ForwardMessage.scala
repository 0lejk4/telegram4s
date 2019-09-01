package telegram4s.methods.messages

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId
import telegram4s.models.messages.TelegramMessage

/**
  * Use this method to forward messages of any kind.
  * On success, the sent Message is returned.
  *
  * @param chatId              Unique identifier for the target chat or username of the target channel
  *                            (in the format @channelusername)
  * @param fromChatId          Unique identifier for the chat where the original message was sent
  *                            (or channel username in the format @channelusername)
  * @param messageId           Unique message identifier
  * @param disableNotification Sends the message silently. iOS users will not receive a notification,
  *                            Android users will receive a notification with no sound.
  */
final case class ForwardMessage(chatId: ChatId,
                                fromChatId: ChatId,
                                messageId: Int,
                                disableNotification: Option[Boolean] = None)

object ForwardMessage {
  implicit val method: TelegramMethod[ForwardMessage, TelegramMessage] =
    TelegramMethod[ForwardMessage, TelegramMessage]("ForwardMessage")
}
