package telegram4s

import telegram4s.models.ResponseParameters

case class Telegram4sException(message: String,
                               errorCode: Int,
                               cause: Option[Throwable] = None,
                               parameters: Option[ResponseParameters] = None) extends RuntimeException(message, cause.orNull)