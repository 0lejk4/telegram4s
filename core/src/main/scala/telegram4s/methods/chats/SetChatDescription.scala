package telegram4s.methods.chats

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId

/**
 * Use this method to change the description of a supergroup or a channel.
 *
 * The bot must be an administrator in the chat for this to work and
 * must have the appropriate admin rights.
 *
 * Returns True on success.
 *
 * @param chatId      Unique identifier for the target chat or username of the target channel
 *                    (in the format @channelusername)
 * @param description New chat description, 0-255 characters
 */
final case class SetChatDescription(chatId: ChatId, description: Option[String] = None)

object SetChatDescription {
  implicit val encoder: Encoder[SetChatDescription] = deriveEncoder[SetChatDescription]
  implicit val method: TelegramMethod[SetChatDescription, Boolean] = TelegramMethod[SetChatDescription, Boolean]("SetChatDescription")
}
