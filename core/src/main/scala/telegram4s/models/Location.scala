package telegram4s.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

/**
 * Represents a point on the map.
 */
final case class Location(longitude: Double, latitude: Double)

object Location {
  implicit val decoder: Decoder[Location] = deriveDecoder[Location]
}
