package telegram4s.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

/**
 * Represents Telegram voice note
 */
final case class Voice(fileId: String, duration: Int, mimeType: Option[String], fileSize: Option[Int])

object Voice {
  implicit val decoder: Decoder[Voice] = deriveDecoder[Voice]
}
