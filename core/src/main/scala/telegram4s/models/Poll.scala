package telegram4s.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

/**
 * Represents a poll in one of the Telegram chats.
 *
 * @param id       Unique poll identifier
 * @param question Poll question, 1-255 characters
 * @param options  List of poll options
 * @param isClosed True, if the poll is calosed
 */
final case class Poll(id: String, question: String, options: List[PollOption], isClosed: Boolean)

object Poll {
  implicit val decoder: Decoder[Poll] = deriveDecoder[Poll]
}
