package telegram4s.methods.messages

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.{ChatId, InlineKeyboardMarkup, Poll}

/**
 * Use this method to stop a poll which was sent by the bot.
 *
 * On success, the stopped Poll with the final results is returned.
 *
 * @param chatId      Unique identifier for the target chat or username of the target channel
 *                    (in the format @channelusername).
 * @param messageId   Identifier of the original message with the poll
 * @param replyMarkup New inline keyboard.
 */
final case class StopPoll(chatId: ChatId, messageId: Int, replyMarkup: Option[InlineKeyboardMarkup] = None)

object StopPoll {
  implicit val encoder: Encoder[StopPoll] = deriveEncoder[StopPoll]
  implicit val method: TelegramMethod[StopPoll, Poll] =
    TelegramMethod[StopPoll, Poll]("StopPoll")
}
