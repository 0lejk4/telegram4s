package telegram4s.methods.games

import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.ChatId
import telegram4s.models.messages.TelegramMessage

/**
 * Use this method to set the score of the specified user in a game.
 *
 * On success, if the message was sent by the bot, returns the edited Message,
 * otherwise returns True.
 * Returns an error, if the new score is not greater than the user's current
 * score in the chat and force is False.
 *
 * Use methods in companion object in order to construct the value of this class.
 *
 * @param userId             User identifier
 * @param score              New score, must be positive
 * @param force              Pass True, if the high score is allowed to decrease.
 *                           This can be useful when fixing mistakes or banning cheaters
 * @param disableEditMessage Pass True, if the game message should not be automatically edited to include the current scoreboard
 * @param chatId             Unique identifier for the target chat (or username of the target channel in the format @channelusername).
 *                           Required if 'inlineMessageId' is not specified.
 * @param messageId          Unique identifier of the sent message.
 *                           Required if 'inlineMessageId' is not specified.
 * @param inlineMessageId    Identifier of the inline message.
 *                           Required if 'chatId' and 'messageId' are not specified.
 */
final class SetGameScore private(val userId: Int,
                                 val score: Long,
                                 val force: Option[Boolean],
                                 val disableEditMessage: Option[Boolean],
                                 val chatId: Option[ChatId] = None,
                                 val messageId: Option[Int] = None,
                                 val inlineMessageId: Option[String] = None)

object SetGameScore {
  implicit val encoder: Encoder[SetGameScore] = deriveEncoder[SetGameScore]
  implicit val method: TelegramMethod[SetGameScore, Either[Boolean, TelegramMessage]] =
    TelegramMethod[SetGameScore, Either[Boolean, TelegramMessage]]("SetGameScore")

  /**
   * For the messages sent directly by the bot
   */
  def direct(chatId: ChatId,
             messageId: Int,
             userId: Int,
             score: Long,
             force: Option[Boolean] = None,
             disableEditMessage: Option[Boolean] = None): SetGameScore =
    new SetGameScore(userId, score, force, disableEditMessage, Some(chatId), Some(messageId))

  /**
   * For the inlined messages sent via the bot
   */
  def inlined(inlineMessageId: String,
              userId: Int,
              score: Long,
              force: Option[Boolean] = None,
              disableEditMessage: Option[Boolean] = None): SetGameScore =
    new SetGameScore(userId, score, force, disableEditMessage, inlineMessageId = Some(inlineMessageId))

}
