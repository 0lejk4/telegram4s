package telegram4s.models

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import telegram4s.models.ChatType.ChatType

sealed trait Chat {
  def id: Long
}

object Chat {
  implicit val encoder: Encoder[Chat] = deriveEncoder[Chat]

  /**
   * Decodes chat based on the `type` value of the input Json
   */
  implicit val chatDecoder: Decoder[Chat] = Decoder.instance[Chat] { cursor =>
    cursor
      .get[ChatType]("type")
      .map {
        case ChatType.Private    => deriveDecoder[PrivateChat]
        case ChatType.Group      => deriveDecoder[Group]
        case ChatType.Supergroup => deriveDecoder[Supergroup]
        case ChatType.Channel    => deriveDecoder[Channel]
      }
      .flatMap(_.tryDecode(cursor))
  }
}

final case class PrivateChat(id: Long, username: Option[String], firstName: Option[String], lastName: Option[String])
  extends Chat

final case class Group(id: Long, title: Option[String]) extends Chat

final case class Supergroup(id: Long, title: Option[String], username: Option[String]) extends Chat

final case class Channel(id: Long, title: Option[String], username: Option[String]) extends Chat
