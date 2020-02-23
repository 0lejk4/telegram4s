package telegram4s.methods.chats

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.{ChatId, ChatMember}

/**
 * Use this method to get a list of administrators in a chat.
 *
 * On success, returns a list of ChatMember objects that contains information
 * about all chat administrators except other bots.
 *
 * If the chat is a group or a supergroup and no administrators were appointed,
 * only the creator will be returned.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel
 *               (in the format @channelusername)
 */
final case class GetChatAdministrators(chatId: ChatId)

object GetChatAdministrators {
  implicit val method: TelegramMethod[GetChatAdministrators, List[ChatMember]] = TelegramMethod[GetChatAdministrators, List[ChatMember]]("GetChatAdministrators")
}
