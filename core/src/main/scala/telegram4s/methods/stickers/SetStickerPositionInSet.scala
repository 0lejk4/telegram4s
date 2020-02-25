package telegram4s.methods.stickers

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod

/**
 * Use this method to move a sticker in a set created by the bot to a specific position.
 *
 * Returns True on success.
 *
 * @param sticker  Sticker's fileId
 * @param position New sticker position in the set, zero-based
 */
final case class SetStickerPositionInSet(sticker: String, position: Int)

object SetStickerPositionInSet {
  implicit val encoder: Encoder[SetStickerPositionInSet] = deriveEncoder[SetStickerPositionInSet]
  implicit val method: TelegramMethod[SetStickerPositionInSet, Boolean] =
    TelegramMethod[SetStickerPositionInSet, Boolean]("SetStickerPositionInSet")
}
