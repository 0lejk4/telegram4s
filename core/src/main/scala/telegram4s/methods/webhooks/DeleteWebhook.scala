package telegram4s.methods.webhooks

import io.circe.{Encoder, Json}
import telegram4s.methods.TelegramMethod

/** Use this method to remove webhook integration if you decide to switch back to getUpdates.
 * Returns True on success. Requires no parameters.
 */
case object DeleteWebhook {
  implicit val encoder: Encoder[DeleteWebhook.type] = Encoder.instance(_ => Json.Null)
  implicit val method: TelegramMethod[DeleteWebhook.type, Boolean] =
    TelegramMethod[DeleteWebhook.type, Boolean]("DeleteWebhook")
}
