package telegram4s.models

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

/**
 * @param name            User name
 * @param phoneNumber     User's phone number
 * @param email           User email
 * @param shippingAddress User shipping address
 */
final case class OrderInfo(name: String, phoneNumber: String, email: String, shippingAddress: ShippingAddress)

object OrderInfo {
  implicit val decoder: Decoder[OrderInfo] = deriveDecoder[OrderInfo]
}
