package telegram4s.models.messages

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import telegram4s.models.{Chat, Sticker, User}

final case class StickerMessage(messageId: Int,
                                chat: Chat,
                                date: Int,
                                sticker: Sticker,
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

object StickerMessage {
  implicit val decoder: Decoder[StickerMessage] = deriveDecoder[StickerMessage]
}
