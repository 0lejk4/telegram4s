package telegram4s.methods.users

import io.circe.generic.auto._
import io.circe.{Encoder, Json}
import telegram4s.methods.TelegramMethod
import telegram4s.models.User

/**
 * A simple method for testing your bot's auth token. Requires no parameters.
 * Returns basic information about the bot in form of a User object.
 */
case object GetMe {
  implicit val encoder: Encoder[GetMe.type] = Encoder.instance(_ => Json.Null)
  implicit val method: TelegramMethod[GetMe.type, User] =
    TelegramMethod[GetMe.type, User]("GetMe")
}
