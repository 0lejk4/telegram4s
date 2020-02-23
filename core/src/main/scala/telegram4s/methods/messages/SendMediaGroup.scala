package telegram4s.methods.messages

import io.circe.Encoder
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.messages.TelegramMessage
import telegram4s.models.{ChatId, InputFile, InputMedia}

/**
 * Use this method to send a group of photos or videos as an album.
 * On success, an array of the sent Messages is returned.
 *
 * @param chatId              Unique identifier for the target chat or username of the target channel
 *                            (in the format @channelusername)
 * @param media               List of photos and videos to be sent, must include 2–10 items
 * @param disableNotification Sends the message silently.
 *                            iOS users will not receive a notification,
 *                            Android users will receive a notification with no sound
 * @param replyToMessageId    If the message is a reply, ID of the original message
 */
final case class SendMediaGroup(chatId: ChatId,
                                media: List[InputMedia],
                                disableNotification: Option[Boolean] = None,
                                replyToMessageId: Option[Int] = None)

object SendMediaGroup {
  import io.circe.generic.auto._
  implicit val encoder: Encoder[SendMediaGroup] = deriveEncoder[SendMediaGroup]
    .contramap[SendMediaGroup](
      s =>
        s.copy(media = s.media.filter(_.media match {
          case InputFile.Upload(_, _) => false
          case InputFile.Existing(_)  => true
        }))
    )
  implicit val method: TelegramMethod[SendMediaGroup, List[TelegramMessage]] =
    TelegramMethod[SendMediaGroup, List[TelegramMessage]](
      name = "SendMediaGroup",
      attachments = _.media.flatMap(_.files)
    )
}
