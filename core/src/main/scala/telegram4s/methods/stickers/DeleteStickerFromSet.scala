package telegram4s.methods.stickers

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod

/**
 * Use this method to delete a sticker from a set created by the bot.
 *
 * Returns True on success.
 *
 * @param sticker Sticker's fileId
 */
final case class DeleteStickerFromSet(sticker: String)

object DeleteStickerFromSet {
  implicit val encoder: Encoder[DeleteStickerFromSet] = deriveEncoder[DeleteStickerFromSet]
  implicit val method: TelegramMethod[DeleteStickerFromSet, Boolean] =
    TelegramMethod[DeleteStickerFromSet, Boolean]("DeleteStickerFromSet")
}
