package telegram4s.methods.messages

import io.circe._
import io.circe.generic.semiauto._
import telegram4s.methods.TelegramMethod
import telegram4s.models.messages.ContactMessage
import telegram4s.models.{ChatId, ReplyMarkup}

/**
 * Use this method to send phone contacts.
 * On success, the sent Message is returned.
 *
 * @param chatId              Unique identifier for the target chat or username of the target channel
 *                            (in the format @channelusername)
 * @param phoneNumber         Contact's phone number
 * @param firstName           Contact's first name
 * @param lastName            Contact's last name
 * @param vcard               Additional data about the contact in the form of a vCard, 0-2048 bytes
 * @param disableNotification Sends the message silently.
 *                            iOS users will not receive a notification,
 *                            Android users will receive a notification with no sound
 * @param replyToMessageId    If the message is a reply, ID of the original message
 * @param replyMarkup         Additional interface options.
 *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
 *                            instructions to hide reply keyboard or to force a reply from the user.
 *
 */
final case class SendContact(chatId: ChatId,
                             phoneNumber: String,
                             firstName: String,
                             lastName: Option[String] = None,
                             vcard: Option[String] = None,
                             disableNotification: Option[Boolean] = None,
                             replyToMessageId: Option[Int] = None,
                             replyMarkup: Option[ReplyMarkup] = None)

object SendContact {
  implicit val encoder: Encoder[SendContact] = deriveEncoder[SendContact]
  implicit val method: TelegramMethod[SendContact, ContactMessage] =
    TelegramMethod[SendContact, ContactMessage]("SendContact")
}
