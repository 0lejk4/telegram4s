package telegram4s.models

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

/**
 * @param id     Shipping option identifier
 * @param title  Option title
 * @param prices List of price portions
 */
final case class ShippingOption(id: String, title: String, prices: List[LabeledPrice])

object ShippingOption {
  implicit val encoder: Encoder[ShippingOption] = deriveEncoder[ShippingOption]
}
