package telegram4s.methods.chats

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId

/**
 * Use this method to unban a previously kicked user in a supergroup.
 * The user will not return to the group automatically, but will be able to join via link, etc.
 *
 * The bot must be an administrator in the group for this to work.
 * Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel
 *               (in the format @channelusername)
 * @param userId Unique identifier of the target user
 */
final case class UnbanChatMember(chatId: ChatId, userId: Int)

object UnbanChatMember {
  implicit val encoder: Encoder[UnbanChatMember] = deriveEncoder[UnbanChatMember]
  implicit val method: TelegramMethod[UnbanChatMember, Boolean] = TelegramMethod[UnbanChatMember, Boolean]("UnbanChatMember")
}
