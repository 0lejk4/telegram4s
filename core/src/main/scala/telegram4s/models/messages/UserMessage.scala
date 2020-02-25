package telegram4s.models.messages

import cats.syntax.functor._
import io.circe.Decoder
import telegram4s.models.{Chat, User}

trait UserMessage extends TelegramMessage {

  def from: Option[User]

  def forwardFrom: Option[User]

  def forwardFromChat: Option[Chat]

  def forwardFromMessageId: Option[Int]

  def forwardSignature: Option[String]

  def forwardSenderName: Option[String]

  def forwardDate: Option[Int]

  def replyToMessage: Option[TelegramMessage]

  def editDate: Option[Int]

  def authorSignature: Option[String]
}

object UserMessage {

  implicit val userMessageDecoder: Decoder[UserMessage] =
    List[Decoder[UserMessage]](
      Decoder[AnimationMessage].widen,
      Decoder[AudioMessage].widen,
      Decoder[ContactMessage].widen,
      Decoder[DocumentMessage].widen,
      Decoder[GameMessage].widen,
      Decoder[InvoiceMessage].widen,
      Decoder[LocationMessage].widen,
      Decoder[PhotoMessage].widen,
      Decoder[PollMessage].widen,
      Decoder[StickerMessage].widen,
      Decoder[TextMessage].widen,
      Decoder[VenueMessage].widen,
      Decoder[VideoMessage].widen,
      Decoder[VideoNoteMessage].widen,
      Decoder[VoiceMessage].widen
    ).reduceLeft(_.or(_))
}
