package telegram4s.methods.messages

import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.messages.TelegramMessage
import telegram4s.models.{ChatId, InlineKeyboardMarkup}

/**
 * Use this method to edit only the reply markup of messages sent by the bot
 * or via the bot (for inline bots).
 *
 * On success, if edited message is sent by the bot, the edited Message is returned,
 * otherwise True is returned.
 *
 * Use methods in companion object in order to construct the value of this class.
 *
 * @param chatId          Unique identifier for the target chat or username of the target channel
 *                        (in the format @channelusername).
 *                        Required if 'inlineMessageId' is not specified.
 * @param messageId       Unique identifier of the sent message.
 *                        Required if 'inlineMessageId' is not specified.
 * @param inlineMessageId Identifier of the inline message.
 *                        Required if 'chatId' and 'messageId' are not specified.
 * @param replyMarkup     New inline keyboard.
 */
final class EditMessageReplyMarkup private(val chatId: Option[ChatId],
                                           val messageId: Option[Int],
                                           val inlineMessageId: Option[String],
                                           val replyMarkup: Option[InlineKeyboardMarkup])

object EditMessageReplyMarkup {
  implicit val encoder: Encoder[EditMessageReplyMarkup] = deriveEncoder[EditMessageReplyMarkup]
  implicit val method: TelegramMethod[EditMessageReplyMarkup, Either[Boolean, TelegramMessage]] =
    TelegramMethod[EditMessageReplyMarkup, Either[Boolean, TelegramMessage]]("EditMessageReplyMarkup")

  /**
   * For the messages sent directly by the bot
   */
  def direct(chatId: ChatId, messageId: Int, replyMarkup: Option[InlineKeyboardMarkup] = None): EditMessageReplyMarkup =
    new EditMessageReplyMarkup(Some(chatId), Some(messageId), None, replyMarkup)

  /**
   * For the inlined messages sent via the bot
   */
  def inlined(inlineMessageId: String, replyMarkup: Option[InlineKeyboardMarkup] = None): EditMessageReplyMarkup =
    new EditMessageReplyMarkup(None, None, Some(inlineMessageId), replyMarkup)
}
