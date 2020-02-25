package telegram4s.methods.messages

import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.messages.TelegramMessage
import telegram4s.models.{ChatId, InlineKeyboardMarkup}

/**
 * Use this method to stop updating a live location message
 * sent by the bot or via the bot (for inline bots) before live_period expires.
 *
 * Use methods in companion object in order to construct the value of this class.
 *
 * @param chatId          Unique identifier for the target chat or username of the target channel
 *                        (in the format @channelusername)
 *                        Required if 'inlineMessageId' is not specified.
 * @param messageId       Identifier of the sent message.
 *                        Required if 'inlineMessageId' is not specified.
 * @param inlineMessageId Identifier of the inline message.
 *                        Required if 'chatId' and 'messageId' are not specified.
 * @param replyMarkup     Additional interface options.
 *                        A JSON-serialized object for an inline keyboard, custom reply keyboard,
 *                        instructions to hide reply keyboard or to force a reply from the user.
 */
final class StopMessageLiveLocation private(val chatId: Option[ChatId],
                                            val messageId: Option[Int],
                                            val inlineMessageId: Option[Int],
                                            val replyMarkup: Option[InlineKeyboardMarkup])

object StopMessageLiveLocation {
  implicit val encoder: Encoder[StopMessageLiveLocation] = deriveEncoder[StopMessageLiveLocation]
  implicit val method: TelegramMethod[StopMessageLiveLocation, Either[Boolean, TelegramMessage]] =
    TelegramMethod[StopMessageLiveLocation, Either[Boolean, TelegramMessage]]("StopMessageLiveLocation")

  /**
   * For the messages sent directly by the bot
   */
  def direct(chatId: ChatId,
             messageId: Int,
             replyMarkup: Option[InlineKeyboardMarkup] = None): StopMessageLiveLocation =
    new StopMessageLiveLocation(Some(chatId), Some(messageId), None, replyMarkup)

  /**
   * For the inlined messages sent via the bot
   */
  def inlined(inlineMessageId: Int, replyMarkup: Option[InlineKeyboardMarkup] = None): StopMessageLiveLocation =
    new StopMessageLiveLocation(None, None, Some(inlineMessageId), replyMarkup)
}
