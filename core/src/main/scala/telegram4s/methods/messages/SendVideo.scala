package telegram4s.methods.messages

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ParseMode.ParseMode
import telegram4s.models.messages.VideoMessage
import telegram4s.models.{ChatId, InputFile, ReplyMarkup}

/**
 * Use this method to send video files, Telegram clients support mp4 videos (other formats may be sent as Document).
 * On success, the sent Message is returned.
 * Bots can currently send video files of up to 50 MB in size, this limit may be changed in the future.
 *
 * @param chatId              Unique identifier for the target chat or username of the target channel
 *                            (in the format @channelusername)
 * @param video               Video to send
 * @param duration            Duration of sent video in seconds
 * @param width               Video width
 * @param height              Video height
 * @param thumb               Thumbnail of the file sent
 * @param caption             Video caption (may also be used when resending videos by file_id), 0-200 characters
 * @param parseMode           Parse mode of captured text (Markdown or HTML)
 * @param supportsStreaming   Pass True, if the uploaded video is suitable for streaming
 * @param disableNotification Sends the message silently.
 *                            iOS users will not receive a notification, Android users will receive a notification with no sound.
 * @param replyToMessageId    If the message is a reply, ID of the original message
 * @param replyMarkup         Additional interface options.
 *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
 *                            instructions to hide reply keyboard or to force a reply from the user.
 */
final case class SendVideo(chatId: ChatId,
                           video: InputFile,
                           duration: Option[Int] = None,
                           width: Option[Int] = None,
                           height: Option[Int] = None,
                           thumb: Option[InputFile] = None,
                           caption: Option[String] = None,
                           parseMode: Option[ParseMode] = None,
                           supportsStreaming: Option[Boolean] = None,
                           disableNotification: Option[Boolean] = None,
                           replyToMessageId: Option[Int] = None,
                           replyMarkup: Option[ReplyMarkup] = None)

object SendVideo {
  implicit val method: TelegramMethod[SendVideo, VideoMessage] =
    TelegramMethod[SendVideo, VideoMessage](
      name = "SendVideo",
      attachments = r => List("video" -> r.video)
    )
}
