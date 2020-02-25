package telegram4s.methods.games

import io.circe._
import io.circe.generic.auto._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.{ChatId, GameHighScore}

/**
 * Use this method to get data for high score tables.
 *
 * Will return the score of the specified user and several of his neighbors in a game.
 * On success, returns a list of GameHighScore objects.
 *
 * This method will currently return scores for the target user, plus two of his closest neighbors on each side.
 * Will also return the top three users if the user and his neighbors are not among them.
 * Please note that this behavior is subject to change.
 *
 * Use methods in companion object in order to construct the value of this class.
 *
 * @param userId          Target user id
 * @param chatId          Unique identifier for the target chat (or username of the target channel in the format @channelusername).
 *                        Required if 'inlineMessageId' is not specified.
 * @param messageId       Unique identifier of the sent message.
 *                        Required if 'inlineMessageId' is not specified.
 * @param inlineMessageId Identifier of the inline message.
 *                        Required if 'chatId' and 'messageId' are not specified.
 */
final class GetGameHighScores private(val userId: Int,
                                      val chatId: Option[ChatId] = None,
                                      val messageId: Option[Int] = None,
                                      val inlineMessageId: Option[String] = None)

object GetGameHighScores {
  implicit val encoder: Encoder[GetGameHighScores] = deriveEncoder[GetGameHighScores]
  implicit val method: TelegramMethod[GetGameHighScores, List[GameHighScore]] = TelegramMethod[GetGameHighScores, List[GameHighScore]]("GetGameHighScores")

  /**
   * For the messages sent directly by the bot
   */
  def direct(chatId: ChatId, messageId: Int, userId: Int): GetGameHighScores =
    new GetGameHighScores(userId, Some(chatId), Some(messageId))

  /**
   * For the inlined messages sent via the bot
   */
  def inlined(inlineMessageId: String, userId: Int): GetGameHighScores =
    new GetGameHighScores(userId, inlineMessageId = Some(inlineMessageId))

}
