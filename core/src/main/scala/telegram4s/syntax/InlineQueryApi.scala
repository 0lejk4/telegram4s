package telegram4s.syntax

import telegram4s.Telegram4sClient
import telegram4s.methods.queries.AnswerInlineQuery
import telegram4s.models.{InlineQuery, InlineQueryResult}

/**
  * Telegram API for the inline query.
  * Offers a convenient access to the related Telegram methods.
  */
final class InlineQueryApi(private val query: InlineQuery) extends AnyVal {

  /**
    * Sends the answer to this query
    *
    * @param results No more than 50 results per query are allowed
    */
  def answer[F[_]: Telegram4sClient](results: List[InlineQueryResult],
                                   switchPmText: Option[String] = None,
                                   switchPmParameter: Option[String] = None): F[Boolean] =
    AnswerInlineQuery(query.id, results, switchPmText = switchPmText, switchPmParameter = switchPmParameter).call
}
