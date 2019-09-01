package telegram4s.methods.stickers

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.{File, InputFile}

/**
 * Use this method to upload a .png file with a sticker for later use in createNewStickerSet
 * and addStickerToSet methods (can be used multiple times).
 * Returns the uploaded File on success.
 *
 * @param userId       Integer User identifier of sticker file owner
 * @param pngSticker   InputFile Png image with the sticker, must be up to 512 kilobytes in size,
 *                     dimensions must not exceed 512px, and either width or height must be exactly 512px.
  *                    [[https://core.telegram.org/bots/api#sending-files More info on Sending Files]]
  */
final case class UploadStickerFile(userId: Int, pngSticker: InputFile)

object UploadStickerFile {
  implicit val method: TelegramMethod[UploadStickerFile, File] =
    TelegramMethod[UploadStickerFile, File](
      name = "UploadStickerFile",
      attachments = r => List("png_sticker" -> r.pngSticker)
    )
}
