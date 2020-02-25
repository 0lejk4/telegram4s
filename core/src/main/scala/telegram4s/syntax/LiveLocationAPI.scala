package telegram4s.syntax

import telegram4s.Telegram4sClient
import telegram4s.methods.messages.{EditMessageLiveLocation, StopMessageLiveLocation}
import telegram4s.models.Location
import telegram4s.models.messages.{LocationMessage, TelegramMessage}

/**
 * Telegram API for the live location message.
 * Offers a convenient access to the related Telegram methods.
 */
final class LiveLocationAPI(private val message: LocationMessage) extends AnyVal {

  /**
   * Edits live location message with a new location.
   * This can only be done while live location is active.
   */
  def editLiveLocation[F[_] : Telegram4sClient](location: Location): F[Either[Boolean, TelegramMessage]] =
    EditMessageLiveLocation.direct(message.chat.id, message.messageId, location.latitude, location.longitude).call

  /**
   * Stops updating a live location message.
   */
  def stopLiveLocation[F[_] : Telegram4sClient]: F[Either[Boolean, TelegramMessage]] =
    StopMessageLiveLocation.direct(message.chat.id, message.messageId).call
}
