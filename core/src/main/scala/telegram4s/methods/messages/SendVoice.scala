package telegram4s.methods.messages

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ParseMode.ParseMode
import telegram4s.models.messages.VoiceMessage
import telegram4s.models.{ChatId, InputFile, ReplyMarkup}

/**
 * Use this method to send audio files, if you want Telegram clients to display the file as a playable voice message.
 * For this to work, your audio must be in an .ogg file encoded with OPUS (other formats may be sent as Audio or Document).
 * On success, the sent Message is returned.
 * Bots can currently send voice messages of up to 50 MB in size, this limit may be changed in the future.
 *
 * @param chatId              Unique identifier for the target chat or username of the target channel
 *                            (in the format @channelusername)
 * @param voice               Audio file to send
 * @param caption             Video caption (may also be used when resending videos by file_id), 0-200 characters
 * @param parseMode           Parse mode of captured text (Markdown or HTML)
 * @param duration            Duration of sent audio in seconds
 * @param disableNotification Sends the message silently.
 *                            iOS users will not receive a notification,
 *                            Android users will receive a notification with no sound
 * @param replyToMessageId    If the message is a reply, ID of the original message
 * @param replyMarkup         Additional interface options.
 *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
 *                            instructions to hide reply keyboard or to force a reply from the user.
 */
final case class SendVoice(chatId: ChatId,
                           voice: InputFile,
                           caption: Option[String] = None,
                           parseMode: Option[ParseMode] = None,
                           duration: Option[Int] = None,
                           disableNotification: Option[Boolean] = None,
                           replyToMessageId: Option[Int] = None,
                           replyMarkup: Option[ReplyMarkup] = None)

object SendVoice {
  implicit val encoder: Encoder[SendVoice] = deriveEncoder[SendVoice]
  implicit val method: TelegramMethod[SendVoice, VoiceMessage] =
    TelegramMethod[SendVoice, VoiceMessage](
      name = "SendVoice",
      attachments = r => List("voice" -> r.voice)
    )
}
