package telegram4s.methods.chats

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId

/**
 * Use this method to kick a user from a group, a supergroup or a channel.
 * In the case of supergroups and channels, the user will not be able to return to the group on their own
 * using invite links, etc., unless unbanned first.
 *
 * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
 *
 * Returns True on success.
 *
 * @param chatId    Unique identifier for the target chat or username of the target channel
 *                  (in the format @channelusername)
 * @param userId    Unique identifier of the target user
 * @param untilDate Date when the user will be unbanned, unix time.
 *                  If user is banned for more than 366 days or less than 30 seconds from the current time
 *                  they are considered to be banned forever
 */
final case class KickChatMember(chatId: ChatId, userId: Int, untilDate: Option[Int] = None)

object KickChatMember {
  implicit val encoder: Encoder[KickChatMember] = deriveEncoder[KickChatMember]
  implicit val method: TelegramMethod[KickChatMember, Boolean] = TelegramMethod[KickChatMember, Boolean]("KickChatMember")
}
