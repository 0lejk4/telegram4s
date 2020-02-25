package telegram4s.models

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import telegram4s.models.MaskPositionType.MaskPositionType

/**
 * The position on faces where a mask should be placed by default.
 *
 * @param point  The part of the face relative to which the mask should be placed.
 *               One of “forehead”, “eyes”, “mouth”, or “chin”.
 * @param xShift Shift by X-axis measured in widths of the mask scaled to the face size, from left to right.
 *               For example, choosing -1.0 will place mask just to the left of the default mask position.
 * @param yShift Shift by Y-axis measured in heights of the mask scaled to the face size, from top to bottom.
 *               For example, 1.0 will place the mask just below the default mask position.
 * @param scale  Mask scaling coefficient. For example, 2.0 means double size.
 */
final case class MaskPosition(point: MaskPositionType, xShift: Double, yShift: Double, scale: Double)

object MaskPosition {
  implicit val encoder: Encoder[MaskPosition] = deriveEncoder[MaskPosition]
  implicit val decoder: Decoder[MaskPosition] = deriveDecoder[MaskPosition]
}
