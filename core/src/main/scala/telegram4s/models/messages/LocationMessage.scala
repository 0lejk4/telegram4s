package telegram4s.models.messages

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import telegram4s.models.{Chat, Location, User}

final case class LocationMessage(messageId: Int,
                                 chat: Chat,
                                 date: Int,
                                 location: Location,
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

object LocationMessage {
  implicit val decoder: Decoder[LocationMessage] = deriveDecoder[LocationMessage]
}
