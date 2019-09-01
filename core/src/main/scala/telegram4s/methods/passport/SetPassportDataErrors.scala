package telegram4s.methods.passport

import io.circe.generic.auto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.passport.PassportElementError

/**
  * Informs a user that some of the Telegram Passport elements they provided contains errors.
  * The user will not be able to re-submit their Passport to you until the errors are fixed
  * (the contents of the field for which you returned the error must change).
  *
  * Returns True on success.
  */
final case class SetPassportDataErrors(userId: Int, errors: List[PassportElementError])

object SetPassportDataErrors {
  implicit val method: TelegramMethod[SetPassportDataErrors, Boolean] =
    TelegramMethod[SetPassportDataErrors, Boolean]("SetPassportDataErrors")
}
