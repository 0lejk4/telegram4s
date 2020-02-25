package telegram4s.methods.chats

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId

/**
 * Use this method to delete a chat photo.
 * Photos can't be changed for private chats.
 *
 * The bot must be an administrator in the chat for this to work and
 * must have the appropriate admin rights.
 *
 * Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel
 *               (in the format @channelusername)
 */
final case class DeleteChatPhoto(chatId: ChatId)

object DeleteChatPhoto {
  implicit val encoder: Encoder[DeleteChatPhoto] = deriveEncoder[DeleteChatPhoto]
  implicit val method: TelegramMethod[DeleteChatPhoto, Boolean] = TelegramMethod[DeleteChatPhoto, Boolean]("DeleteChatPhoto")
}
