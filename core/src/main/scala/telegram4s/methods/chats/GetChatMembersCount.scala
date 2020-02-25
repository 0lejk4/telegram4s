package telegram4s.methods.chats

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId

/**
 * Use this method to get the number of members in a chat.
 *
 * Returns Int on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel
 *               (in the format @channelusername)
 */
final case class GetChatMembersCount(chatId: ChatId)

object GetChatMembersCount {
  implicit val encoder: Encoder[GetChatMembersCount] = deriveEncoder[GetChatMembersCount]
  implicit val method: TelegramMethod[GetChatMembersCount, Int] = TelegramMethod[GetChatMembersCount, Int]("GetChatMembersCount")
}
