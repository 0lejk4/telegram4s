package telegram4s.methods.messages

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.messages.StickerMessage
import telegram4s.models.{ChatId, InputFile, ReplyMarkup}

/**
 * Use this method to send stickers.
 * You can send existing stickers (using file_id), static .WEBP or animated .TGS stickers.
 *
 * On success, the sent Message is returned.
 *
 * @param chatId              Unique identifier for the target chat or username of the target channel
 *                            (in the format @channelusername)
 * @param sticker             Sticker to send.
 * @param disableNotification Sends the message silently.
 *                            iOS users will not receive a notification,
 *                            Android users will receive a notification with no sound
 * @param replyToMessageId    If the message is a reply, ID of the original message
 * @param replyMarkup         Additional interface options.
 *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
 *                            instructions to hide reply keyboard or to force a reply from the user.
 */
final case class SendSticker(chatId: ChatId,
                             sticker: InputFile,
                             disableNotification: Option[Boolean] = None,
                             replyToMessageId: Option[Int] = None,
                             replyMarkup: Option[ReplyMarkup] = None)

object SendSticker {
  implicit val method: TelegramMethod[SendSticker, StickerMessage] =
    TelegramMethod[SendSticker, StickerMessage](
      name = "SendSticker",
      attachments = r => List("sticker" -> r.sticker)
    )
}
