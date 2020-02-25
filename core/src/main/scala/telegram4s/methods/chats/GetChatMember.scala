package telegram4s.methods.chats

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.{ChatId, ChatMember}

/**
 * Use this method to get information about a member of a chat.
 *
 * Returns a ChatMember object on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel
 *               (in the format @channelusername)
 * @param userId Unique identifier of the target user
 */
final case class GetChatMember(chatId: ChatId, userId: Int)

object GetChatMember {
  implicit val encoder: Encoder[GetChatMember] = deriveEncoder[GetChatMember]
  implicit val method: TelegramMethod[GetChatMember, ChatMember] = TelegramMethod[GetChatMember, ChatMember]("GetChatMember")
}
