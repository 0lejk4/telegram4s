package telegram4s.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

/**
 * Represents a general file (as opposed to photos, voice messages and audio files).
 *
 * @param fileId   Unique identifier
 * @param thumb    Document thumbnail as defined by sender
 * @param fileName Original filename as defined by sender
 * @param mimeType MIME type of the file as defined by sender
 * @param fileSize File size
 */
final case class Document(fileId: String,
                          thumb: Option[PhotoSize],
                          fileName: Option[String],
                          mimeType: Option[String],
                          fileSize: Option[Int])

object Document {
  implicit val decoder: Decoder[Document] = deriveDecoder[Document]
}
