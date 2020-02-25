package telegram4s.methods.chats

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.{ChatId, InputFile}

/**
 * Use this method to set a new profile photo for the chat.
 * Photos can't be changed for private chats.
 *
 * The bot must be an administrator in the chat for this to work and
 * must have the appropriate admin rights.
 *
 * Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel
 *               (in the format @channelusername)
 * @param photo  New chat photo
 */
final case class SetChatPhoto(chatId: ChatId, photo: InputFile)

object SetChatPhoto {
  implicit val encoder: Encoder[SetChatPhoto] = deriveEncoder[SetChatPhoto]
  implicit val method: TelegramMethod[SetChatPhoto, Boolean] = TelegramMethod[SetChatPhoto, Boolean]("SetChatPhoto")
}
