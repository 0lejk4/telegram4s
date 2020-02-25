package telegram4s.models

import io.circe._
import io.circe.generic.semiauto._

/**
 * Telegram user or bot.
 *
 * @param id           Unique identifier
 * @param isBot        True, if this user is a bot
 * @param firstName    User's or bot's first name
 * @param lastName     User's or bot's last name
 * @param username     User's or bot's username
 * @param languageCode IETF language tag of the user's language
 */
final case class User(id: Int,
                      isBot: Boolean,
                      firstName: String,
                      lastName: Option[String],
                      username: Option[String],
                      languageCode: Option[String])

object User {
  implicit val decoder: Decoder[User] = deriveDecoder[User]
}
