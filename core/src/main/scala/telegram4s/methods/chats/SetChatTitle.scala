package telegram4s.methods.chats

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId

/**
  * Use this method to change the title of a chat.
  * Titles can't be changed for private chats.
  *
  * The bot must be an administrator in the chat for this to work and
  * must have the appropriate admin rights.
  *
  * Returns True on success.
  *
  * @param chatId Unique identifier for the target chat or username of the target channel
  *               (in the format @channelusername)
  * @param title  New chat title, 1-255 characters
  */
final case class SetChatTitle(chatId: ChatId, title: String)

object SetChatTitle {
  implicit val method: TelegramMethod[SetChatTitle, Boolean] = TelegramMethod[SetChatTitle, Boolean]("SetChatTitle")
}
