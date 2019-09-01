package telegram4s.methods.chats

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId

/**
  * Use this method to pin a message in a group, a supergroup, or a channel.
  *
  * The bot must be an administrator in the chat for this to work and
  * must have the ‘can_pin_messages’ admin right in the supergroup
  * or ‘can_edit_messages’ admin right in the channel.
  *
  * @param chatId              Unique identifier for the target chat or username of the target channel
  *                            (in the format @channelusername)
  * @param messageId           Identifier of a message to pin
  * @param disableNotification True, if it is not necessary to send a notification to all chat members about the new pinned message.
  *                            Notifications are always disabled in channels.
  */
final case class PinChatMessage(chatId: ChatId, messageId: Int, disableNotification: Option[Boolean] = None)

object PinChatMessage {
  implicit val method: TelegramMethod[PinChatMessage, Boolean] = TelegramMethod[PinChatMessage, Boolean]("PinChatMessage")
}
