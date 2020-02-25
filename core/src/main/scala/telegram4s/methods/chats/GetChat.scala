package telegram4s.methods.chats

import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.{ChatId, DetailedChat}

/**
 * Use this method to get up to date information about the chat (current name of the user
 * for one-on-one conversations, current username of a user, group or channel, etc.)
 *
 * Returns a DetailedChat object on success.
 *
 * @param chatId Unique identifier for the target chat or username of the target channel
 *               (in the format @channelusername)
 */
final case class GetChat(chatId: ChatId)

object GetChat {
  implicit val encoder: Encoder[GetChat] = deriveEncoder[GetChat]
  implicit val method: TelegramMethod[GetChat, DetailedChat] = TelegramMethod[GetChat, DetailedChat]("GetChat")
}
