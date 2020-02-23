package telegram4s.methods.chats

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatAction.ChatAction
import telegram4s.models.ChatId

/**
  * Use this method when you need to tell the user that something is happening on the bot's side.
  * The status is set for 5 seconds or less (when a message arrives from your bot,
  * Telegram clients clear its typing status).
  *
  * Example: The ImageBot needs some time to process a request and upload the image.
  * Instead of sending a text message along the lines of "Retrieving image, please wait...",
  * the bot may use sendChatAction with action = upload_photo.
  * The user will see a "sending photo" status for the bot.
  * We only recommend using this method when a response from the bot will take
  * a noticeable amount of time to arrive.
  *
  * @param chatId Unique identifier for the target chat or username of the target channel
  *               (in the format @channelusername)
  * @param action Type of action to broadcast
  */
final case class SendChatAction(chatId: ChatId, action: ChatAction)

object SendChatAction {
  implicit val method: TelegramMethod[SendChatAction, Boolean] = TelegramMethod[SendChatAction, Boolean]("SendChatAction")
}
