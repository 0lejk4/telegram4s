package telegram4s.syntax

import telegram4s.Telegram4sClient
import telegram4s.methods.queries.AnswerPreCheckoutQuery
import telegram4s.models.PreCheckoutQuery

/**
  * Telegram API for the pre checkout query.
  * Offers a convenient access to the related Telegram methods.
  */
final class PreCheckoutQueryAPI(private val query: PreCheckoutQuery) extends AnyVal {

  /**
    * Signals that the bot is ready to proceed with the order.
    */
  def confirm[F[_]: Telegram4sClient]: F[Boolean] =
    AnswerPreCheckoutQuery.positive(query.id).call

  /**
    * Signals that there's a problem with this order.
    *
    * @param message Human readable form that explains the reason for failure
    */
  def reject[F[_]: Telegram4sClient](message: String): F[Boolean] =
    AnswerPreCheckoutQuery.negative(query.id, message).call
}
