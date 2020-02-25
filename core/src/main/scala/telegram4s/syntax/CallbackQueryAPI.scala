package telegram4s.syntax

import telegram4s.Telegram4sClient
import telegram4s.methods.queries.AnswerCallbackQuery
import telegram4s.models.CallbackQuery

/**
 * Telegram API for the callback query.
 * Offers a convenient access to the related Telegram methods.
 */
final class CallbackQueryAPI(private val query: CallbackQuery) extends AnyVal {

  /**
   * Notify the user with the provided text at the top of the chat screen
   */
  def inform[F[_] : Telegram4sClient](text: String): F[Boolean] =
    AnswerCallbackQuery.notification(query.id, text).call

  /**
   * Notify the user with the provided text in a pop-up form
   */
  def alert[F[_] : Telegram4sClient](text: String): F[Boolean] =
    AnswerCallbackQuery.alert(query.id, text).call

  /**
   * Redirect the user to the provided address.
   *
   * Example: you can redirect a user to your bot using `telegram.me/your_bot?start=XXXX`
   */
  def redirect[F[_] : Telegram4sClient](url: String): F[Boolean] =
    AnswerCallbackQuery.redirect(query.id, url).call

}
