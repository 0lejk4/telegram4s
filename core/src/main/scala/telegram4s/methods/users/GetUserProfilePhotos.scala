package telegram4s.methods.users

import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.UserProfilePhotos

/**
 * Use this method to get a list of profile pictures for a user.
 *
 * @param userId Unique identifier of the target user
 * @param offset Sequential number of the first photo to be returned.
 *               By default, all photos are returned.
 * @param limit  Limits the number of photos to be retrieved.
 *               Values between 1-100 are accepted. Defaults to 100.
 */
final case class GetUserProfilePhotos(userId: Int, offset: Option[Int] = None, limit: Option[Int] = None)

object GetUserProfilePhotos {
  implicit val encoder: Encoder[GetUserProfilePhotos] = deriveEncoder[GetUserProfilePhotos]
  implicit val method: TelegramMethod[GetUserProfilePhotos, UserProfilePhotos] =
    TelegramMethod[GetUserProfilePhotos, UserProfilePhotos]("GetUserProfilePhotos")
}
