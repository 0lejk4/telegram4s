package telegram4s.methods.queries

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.InlineQueryResult

/** Use this method to send answers to an inline query.
  * No more than 50 results per query are allowed.
  *
  * On success, True is returned.
  *
  * @param inlineQueryId     Unique identifier for the answered query
  * @param results           List of results for the inline query
  * @param cacheTime         The maximum amount of time in seconds that the result of the inline query may be cached on the server.
  *                          Defaults to 300.
  * @param isPersonal        Pass True, if results may be cached on the server side only for the user that sent the query.
  *                          By default, results may be returned to any user who sends the same query
  * @param nextOffset        Pass the offset that a client should send in the next query with the same text to receive more results.
  *                          Pass an empty string if there are no more results or if you don't support pagination.
  *                          Offset length can't exceed 64 bytes.
  * @param switchPmText      If passed, clients will display a button with specified text that switches the user to
  *                          a private chat with the bot and sends the bot a start message with the parameter `switch_pm_parameter`
  * @param switchPmParameter Parameter for the start message sent to the bot when user presses the switch button.
  *                          Example: An inline bot that sends YouTube videos can ask the user to connect
  *                          the bot to their YouTube account to adapt search results accordingly.
  *                          To do this, it displays a 'Connect your YouTube account' button above the results,
  *                          or even before showing any.
  *                          The user presses the button, switches to a private chat with the bot and,
  *                          in doing so, passes a start parameter that instructs the bot to return an oauth link.
  *                          Once done, the bot can offer a switch_inline button so that the user can easily return
  *                          to the chat where they wanted to use the bot's inline capabilities.
  */
final case class AnswerInlineQuery(inlineQueryId: String,
                                   results: Seq[InlineQueryResult],
                                   cacheTime: Option[Int] = None,
                                   isPersonal: Option[Boolean] = None,
                                   nextOffset: Option[String] = None,
                                   switchPmText: Option[String] = None,
                                   switchPmParameter: Option[String] = None)

object AnswerInlineQuery {
  implicit val method: TelegramMethod[AnswerInlineQuery, Boolean] =
    TelegramMethod[AnswerInlineQuery, Boolean]("AnswerInlineQuery")
}
