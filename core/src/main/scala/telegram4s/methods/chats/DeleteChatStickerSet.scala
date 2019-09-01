package telegram4s.methods.chats

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId

/**
  * Use this method to delete a group sticker set from a supergroup.
  *
  * The bot must be an administrator in the chat for this to work and
  * must have the appropriate admin rights.
  * Use the field can_set_sticker_set optionally returned in getChat requests to
  * check if the bot can use this method.
  *
  * Returns True on success.
  *
  * @param chatId Unique identifier for the target chat or username of the target channel
  *               (in the format @channelusername)
  */
final case class DeleteChatStickerSet(chatId: ChatId)

object DeleteChatStickerSet {
  implicit val method: TelegramMethod[DeleteChatStickerSet, Boolean] = TelegramMethod[DeleteChatStickerSet, Boolean]("DeleteChatStickerSet")
}
