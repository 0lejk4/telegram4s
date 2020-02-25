package telegram4s.methods.messages

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.messages.VideoNoteMessage
import telegram4s.models.{ChatId, InputFile, ReplyMarkup}

/**
 * Use this method to send video messages.
 * On success, the sent Message is returned.
 * As of v.4.0, Telegram clients support rounded square mp4 videos of up to 1 minute long.
 *
 * @param chatId              Unique identifier for the target chat or username of the target channel
 *                            (in the format @channelusername)
 * @param videoNote           Video note to send.
 * @param duration            Duration of sent video in seconds
 * @param length              Video width and height
 * @param disableNotification Sends the message silently.
 *                            iOS users will not receive a notification,
 *                            Android users will receive a notification with no sound
 * @param replyToMessageId    If the message is a reply, ID of the original message
 * @param replyMarkup         Additional interface options.
 *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
 *                            instructions to hide reply keyboard or to force a reply from the user.
 */
final case class SendVideoNote(chatId: ChatId,
                               videoNote: InputFile,
                               duration: Option[Int] = None,
                               length: Option[Int] = None,
                               disableNotification: Option[Boolean] = None,
                               replyToMessageId: Option[Int] = None,
                               replyMarkup: Option[ReplyMarkup] = None)

object SendVideoNote {
  implicit val encoder: Encoder[SendVideoNote] = deriveEncoder[SendVideoNote]
  implicit val method: TelegramMethod[SendVideoNote, VideoNoteMessage] =
    TelegramMethod[SendVideoNote, VideoNoteMessage](
      name = "SendVideoNote",
      attachments = r => List("videoNote" -> r.videoNote)
    )
}
