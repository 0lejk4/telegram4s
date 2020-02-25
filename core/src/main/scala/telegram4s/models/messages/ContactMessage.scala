package telegram4s.models.messages

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.models.{Chat, Contact, User}

final case class ContactMessage(messageId: Int,
                                chat: Chat,
                                date: Int,
                                contact: Contact,
                                from: Option[User] = None,
                                forwardFrom: Option[User] = None,
                                forwardFromChat: Option[Chat] = None,
                                forwardFromMessageId: Option[Int] = None,
                                forwardSignature: Option[String] = None,
                                forwardSenderName: Option[String] = None,
                                forwardDate: Option[Int] = None,
                                replyToMessage: Option[TelegramMessage] = None,
                                editDate: Option[Int] = None,
                                authorSignature: Option[String] = None)
  extends UserMessage

object ContactMessage {
  implicit val decoder: Decoder[ContactMessage] = deriveDecoder[ContactMessage]
}
