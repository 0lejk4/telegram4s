package telegram4s.methods.chats

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId

/**
 * Use this method for your bot to leave a group, supergroup or channel.
 *
 * Returns True on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel
 *               (in the format @channelusername)
 */
final case class LeaveChat(chatId: ChatId)

object LeaveChat {
  implicit val encoder: Encoder[LeaveChat] = deriveEncoder[LeaveChat]
  implicit val method: TelegramMethod[LeaveChat, Boolean] = TelegramMethod[LeaveChat, Boolean]("LeaveChat")
}
