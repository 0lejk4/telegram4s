package telegram4s.syntax

import telegram4s.Telegram4sClient
import telegram4s.methods.messages.StopPoll
import telegram4s.models.messages.PollMessage
import telegram4s.models.{InlineKeyboardMarkup, Poll}

/**
 * Telegram API for the poll message.
 * Offers a convenient access to the related Telegram methods.
 */
final class PollMessageApi(private val pollMessage: PollMessage) extends AnyVal {

  /**
   * Stops the poll sent by the bot.
   */
  def stopPoll[F[_] : Telegram4sClient](markup: Option[InlineKeyboardMarkup] = None): F[Poll] =
    StopPoll(pollMessage.chat.id, pollMessage.messageId, markup).call

}
