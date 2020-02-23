package telegram4s.syntax

import telegram4s.Telegram4sClient
import telegram4s.methods.queries.AnswerShippingQuery
import telegram4s.models.{ShippingOption, ShippingQuery}

/**
  * Telegram API for the shipping query.
  * Offers a convenient access to the related Telegram methods.
  */
final class ShippingQueryAPI(private val query: ShippingQuery) extends AnyVal {

  /**
    * Signals that the delivery to the specified address is possible
    * and presents available shipping options.
    */
  def proceed[F[_]: Telegram4sClient](options: List[ShippingOption]): F[Boolean] =
    AnswerShippingQuery.positive(query.id, options).call

  /**
    * Signals that it's impossible to complete the order.
    *
    * @param message Human readable description of the problem
    */
  def abort[F[_]: Telegram4sClient](message: String): F[Boolean] =
    AnswerShippingQuery.negative(query.id, message).call
}
