package telegram4s.methods.chats

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId

/**
  * Use this method to set a new group sticker set for a supergroup.
  *
  * The bot must be an administrator in the chat for this to work and
  * must have the appropriate admin rights.
  * Use the field can_set_sticker_set optionally returned in getChat requests to check
  * if the bot can use this method.
  *
  * Returns True on success.
  *
  * @param chatId         Unique identifier for the target chat or username of the target channel
  *                       (in the format @channelusername)
  * @param stickerSetName Name of the sticker set to be set as the group sticker set
  */
final case class SetChatStickerSet(chatId: ChatId, stickerSetName: String)

object SetChatStickerSet {
  implicit val method: TelegramMethod[SetChatStickerSet, Boolean] = TelegramMethod[SetChatStickerSet, Boolean]("SetChatStickerSet")
}
