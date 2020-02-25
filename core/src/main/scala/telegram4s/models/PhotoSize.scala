package telegram4s.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

/**
 * Version of a photo or a file / sticker thumbnail of specifi1c size.
 */
final case class PhotoSize(fileId: String, width: Int, height: Int, fileSize: Option[Int])

object PhotoSize {
  implicit val decoder: Decoder[PhotoSize] = deriveDecoder[PhotoSize]
}
