package telegram4s.methods.messages

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ParseMode.ParseMode
import telegram4s.models.messages.PhotoMessage
import telegram4s.models.{ChatId, InputFile, ReplyMarkup}

/**
 * Use this method to send photos.
 * On success, the sent Message is returned.
 *
 * @param chatId              Unique identifier for the target chat or username of the target channel
 *                            (in the format @channelusername)
 * @param photo               Photo to send.
 *                            Pass a file_id as String to send a photo that exists on the Telegram servers (recommended),
 *                            pass an HTTP URL as a String for Telegram to get a photo from the Internet, or upload a new photo using multipart/form-data.
 * @param caption             Photo caption (may also be used when resending photos by file_id), 0-200 characters
 * @param parseMode           Parse mode of captioned text (Markdown or HTML)
 * @param disableNotification Sends the message silently.
 *                            iOS users will not receive a notification,
 *                            Android users will receive a notification with no sound.
 * @param replyToMessageId    If the message is a reply, ID of the original message
 * @param replyMarkup         Additional interface options.
 *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
 *                            instructions to hide reply keyboard or to force a reply from the user.
 */
final case class SendPhoto(chatId: ChatId,
                           photo: InputFile,
                           caption: Option[String] = None,
                           parseMode: Option[ParseMode] = None,
                           disableNotification: Option[Boolean] = None,
                           replyToMessageId: Option[Int] = None,
                           replyMarkup: Option[ReplyMarkup] = None)

object SendPhoto {
  implicit val method: TelegramMethod[SendPhoto, PhotoMessage] =
    TelegramMethod[SendPhoto, PhotoMessage](
      name = "SendPhoto",
      attachments = r => List("photo" -> r.photo)
    )
}
