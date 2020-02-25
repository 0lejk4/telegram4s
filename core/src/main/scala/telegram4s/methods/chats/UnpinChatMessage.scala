package telegram4s.methods.chats

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId

/**
 * Use this method to unpin a message in a group, a supergroup, or a channel.
 *
 * The bot must be an administrator in the chat for this to work and
 * must have the ‘can_pin_messages’ admin right in the supergroup
 * or ‘can_edit_messages’ admin right in the channel.
 *
 * Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel
 *               (in the format @channelusername)
 */
final case class UnpinChatMessage(chatId: ChatId)

object UnpinChatMessage {
  implicit val encoder: Encoder[UnpinChatMessage] = deriveEncoder[UnpinChatMessage]
  implicit val method: TelegramMethod[UnpinChatMessage, Boolean] = TelegramMethod[UnpinChatMessage, Boolean]("UnpinChatMessage")
}
