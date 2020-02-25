package telegram4s.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

/**
 * This object contains information about one answer option in a poll.
 *
 * @param text       Option text, 1-100 characters
 * @param voterCount Number of users that voted for this option
 */
final case class PollOption(text: String, voterCount: Int)

object PollOption {
  implicit val decoder: Decoder[PollOption] = deriveDecoder[PollOption]
}
