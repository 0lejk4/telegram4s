package telegram4s.methods.stickers

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod

/**
 * Use this method to get a sticker set.
 *
 * On success, a StickerSet object is returned.
 *
 * @param name Name of the sticker set
 */
final case class GetStickerSet(name: String)

object GetStickerSet {
  implicit val encoder: Encoder[GetStickerSet] = deriveEncoder[GetStickerSet]
  implicit val method: TelegramMethod[GetStickerSet, Boolean] =
    TelegramMethod[GetStickerSet, Boolean]("GetStickerSet")
}
