package telegram4s.methods.messages

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ParseMode.ParseMode
import telegram4s.models.messages.TelegramMessage
import telegram4s.models.{ChatId, ReplyMarkup}

/**
 * Use this method to edit text messages sent by the bot or via the bot (for inline bots).
 *
 * On success, if edited message is sent by the bot, the edited Message is returned,
 * otherwise True is returned.
 *
  * Use methods in companion object in order to construct the value of this class.
  *
  * @param chatId                Unique identifier for the target chat or username of the target channel
  *                              (in the format @channelusername).
  *                              Required if 'inlineMessageId' is not specified.
  * @param messageId             Unique identifier of the sent message.
  *                              Required if 'inlineMessageId' is not specified.
  * @param inlineMessageId       Identifier of the inline message.
  *                              Required if 'chatId' and 'messageId' are not specified.
  * @param text                  New text of the message
  * @param parseMode             Parse mode of input text (Markdown or HTML)
  * @param disableWebPagePreview Disables link previews for links in this message
  * @param replyMarkup           Additional interface options.
  *                              A JSON-serialized object for an inline keyboard, custom reply keyboard,
  *                              instructions to hide reply keyboard or to force a reply from the user.
  */
final class EditMessageText private (val chatId: Option[ChatId],
                                     val messageId: Option[Int],
                                     val inlineMessageId: Option[String],
                                     val text: String,
                                     val parseMode: Option[ParseMode],
                                     val disableWebPagePreview: Option[Boolean],
                                     val replyMarkup: Option[ReplyMarkup])

object EditMessageText {
  implicit val method: TelegramMethod[EditMessageText, Either[Boolean, TelegramMessage]] =
    TelegramMethod[EditMessageText, Either[Boolean, TelegramMessage]]("EditMessageText")

  /**
   * For the messages sent directly by the bot
   */
  def direct(chatId: ChatId,
             messageId: Int,
             text: String,
             parseMode: Option[ParseMode] = None,
             disableWebPagePreview: Option[Boolean] = None,
             replyMarkup: Option[ReplyMarkup] = None): EditMessageText =
    new EditMessageText(Some(chatId), Some(messageId), None, text, parseMode, disableWebPagePreview, replyMarkup)

  /**
    * For the inlined messages sent via the bot
    */
  def inlined(inlineMessageId: String,
              text: String,
              parseMode: Option[ParseMode] = None,
              disableWebPagePreview: Option[Boolean] = None,
              replyMarkup: Option[ReplyMarkup] = None): EditMessageText =
    new EditMessageText(None, None, Some(inlineMessageId), text, parseMode, disableWebPagePreview, replyMarkup)
}
