package telegram4s.models.messages

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import telegram4s.models.{Chat, Game, User}

final case class GameMessage(messageId: Int,
                             chat: Chat,
                             date: Int,
                             game: Game,
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

object GameMessage {
  implicit val decoder: Decoder[GameMessage] = deriveDecoder[GameMessage]
}
