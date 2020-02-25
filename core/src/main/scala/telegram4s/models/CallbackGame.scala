package telegram4s.models

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder, Json}

/**
 * A placeholder, currently holds no information.
 */
sealed trait CallbackGame

object CallbackGame extends CallbackGame {
  implicit val encoder: Encoder[CallbackGame] = deriveEncoder
  implicit val decoder: Decoder[CallbackGame] = deriveDecoder
  implicit val callbackGameEncoder: Encoder[CallbackGame.type] = Encoder.instance(_ => Json.Null)
  implicit val callbackGameDecoder: Decoder[CallbackGame.type] = Decoder.const(CallbackGame)
}
