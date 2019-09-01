package telegram4s

import telegram4s.models.ResponseParameters

case class TelegramApiException(message: String,
                                errorCode: Int,
                                cause: Option[Throwable] = None,
                                parameters: Option[ResponseParameters] = None) extends RuntimeException(message, cause.orNull)


case class Telegram4sException(message: String, cause: Option[Throwable] = None) extends RuntimeException(message, cause.orNull)