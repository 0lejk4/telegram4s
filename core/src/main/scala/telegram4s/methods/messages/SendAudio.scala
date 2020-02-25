package telegram4s.methods.messages

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ParseMode.ParseMode
import telegram4s.models.messages.AudioMessage
import telegram4s.models.{ChatId, InputFile, ReplyMarkup}

/**
 * Use this method to send audio files, if you want Telegram clients to display them in the music player.
 * Your audio must be in the .mp3 format.
 * On success, the sent Message is returned.
 * Bots can currently send audio files of up to 50 MB in size, this limit may be changed in the future.
 *
 * For sending voice messages, use the sendVoice method instead.
 *
 * @param chatId              Unique identifier for the target chat or username of the target channel
 *                            (in the format @channelusername)
 * @param audio               Audio file to send.
 *                            Pass a file_id as String to send an audio file that exists on the Telegram servers (recommended),
 *                            pass an HTTP URL as a String for Telegram to get an audio file from the Internet, or upload a new one using multipart/form-data.
 * @param caption             Audio caption, 0-200 characters
 * @param parseMode           Parse mode of captioned text (Markdown or HTML)
 * @param duration            Duration of the audio in seconds
 * @param performer           Track performer
 * @param title               Track name
 * @param thumb               Thumbnail of the file sent
 * @param disableNotification Sends the message silently.
 *                            iOS users will not receive a notification,
 *                            Android users will receive a notification with no sound.
 * @param replyToMessageId    If the message is a reply, ID of the original message
 * @param replyMarkup         Additional interface options.
 *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
 *                            instructions to hide reply keyboard or to force a reply from the user.
 */
final case class SendAudio(chatId: ChatId,
                           audio: InputFile,
                           duration: Option[Int] = None,
                           caption: Option[String] = None,
                           parseMode: Option[ParseMode] = None,
                           performer: Option[String] = None,
                           title: Option[String] = None,
                           thumb: Option[InputFile] = None,
                           disableNotification: Option[Boolean] = None,
                           replyToMessageId: Option[Int] = None,
                           replyMarkup: Option[ReplyMarkup] = None)

object SendAudio {
  implicit val encoder: Encoder[SendAudio] = deriveEncoder[SendAudio]
  implicit val method: TelegramMethod[SendAudio, AudioMessage] =
    TelegramMethod[SendAudio, AudioMessage](
      name = "SendAudio",
      attachments = r => List("audio" -> r.audio)
    )
}
