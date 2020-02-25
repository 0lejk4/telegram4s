package telegram4s.methods.chats

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.{ChatId, ChatPermissions}

/**
 * Use this method to set default chat permissions for all members.
 * The bot must be an administrator in the group or a supergroup for this to work
 * and must have the can_restrict_members admin rights.
 *
 * @param chatId      Unique identifier for the target chat or username of the target channel
 *                    (in the format @channelusername)
 * @param permissions New default chat permissions
 */
final case class SetChatPermissions(chatId: ChatId, permissions: ChatPermissions)

object SetChatPermissions {
  implicit val encoder: Encoder[SetChatPermissions] = deriveEncoder[SetChatPermissions]
  implicit val method: TelegramMethod[SetChatPermissions, Boolean] = TelegramMethod[SetChatPermissions, Boolean]("SetChatPermissions")
}
