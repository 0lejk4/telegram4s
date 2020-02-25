package telegram4s.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

/**
 * Represents a video message (available in Telegram apps as of v.4.0).
 *
 * @param fileId   Unique identifier for this file
 * @param length   Video width and height as defined by sender
 * @param duration Duration of the video in seconds as defined by sender
 * @param thumb    Video thumbnail
 * @param fileSize File size
 */
final case class VideoNote(fileId: String, length: Int, duration: Int, thumb: Option[PhotoSize], fileSize: Option[Int])

object VideoNote {
  implicit val decoder: Decoder[VideoNote] = deriveDecoder[VideoNote]
}
