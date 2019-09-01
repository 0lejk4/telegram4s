package telegram4s.methods.webhooks

import io.circe.generic.auto._
import io.circe.{Encoder, Json}
import telegram4s.methods.TelegramMethod
import telegram4s.models.WebhookInfo

/** Use this method to get current webhook status.
 * Requires no parameters. On success, returns a WebhookInfo object.
 * If the bot is using getUpdates, will return an object with the url field empty.
 */
case object GetWebhookInfo {
  implicit val encoder: Encoder[GetWebhookInfo.type] = Encoder.instance(_ => Json.Null)
  implicit val method: TelegramMethod[GetWebhookInfo.type, WebhookInfo] =
    TelegramMethod[GetWebhookInfo.type, WebhookInfo]("GetWebhookInfo")
}
