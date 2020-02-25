package telegram4s.models

import cats.syntax.functor._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import telegram4s.models.ParseMode.ParseMode

/**
 * Content of a message to be sent as a result of an inline query.
 */
sealed trait InlineQueryResultContent

object InlineQueryResultContent {
  implicit val encoder: Encoder[InlineQueryResultContent] =
    Encoder.instance {
      case entity: InlineQueryResultTextContent     => Encoder[InlineQueryResultTextContent].apply(entity)
      case entity: InlineQueryResultLocationContent => Encoder[InlineQueryResultLocationContent].apply(entity)
      case entity: InlineQueryResultVenueContent    => Encoder[InlineQueryResultVenueContent].apply(entity)
      case entity: InlineQueryResultContactContent  => Encoder[InlineQueryResultContactContent].apply(entity)
    }
  implicit val decoder: Decoder[InlineQueryResultContent] =
    List[Decoder[InlineQueryResultContent]](
      InlineQueryResultLocationContent.decoder.widen,
      InlineQueryResultTextContent.decoder.widen,
      InlineQueryResultVenueContent.decoder.widen,
      InlineQueryResultContactContent.decoder.widen,
    ).reduceLeft(_.or(_))
}

/**
 * Content of a text message to be sent as the result of an inline query.
 *
 * @param messageText           Text of the message to be sent, 1-4096 characters
 * @param parseMode             Parse mode of captured text (Markdown or HTML)
 * @param disableWebPagePreview Disables link previews for links in the sent message
 */
final case class InlineQueryResultTextContent(messageText: String,
                                              parseMode: Option[ParseMode] = None,
                                              disableWebPagePreview: Option[Boolean] = None)
  extends InlineQueryResultContent

object InlineQueryResultTextContent {
  implicit val decoder: Decoder[InlineQueryResultTextContent] = deriveDecoder[InlineQueryResultTextContent]
  implicit val encoder: Encoder[InlineQueryResultTextContent] = deriveEncoder[InlineQueryResultTextContent]
}

/**
 * Content of a location message to be sent as the result of an inline query.
 *
 * ''Note:''
 * This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
 *
 * @param latitude  Latitude of the location in degrees
 * @param longitude Longitude of the location in degrees
 */
final case class InlineQueryResultLocationContent(latitude: Double, longitude: Double) extends InlineQueryResultContent

object InlineQueryResultLocationContent {
  implicit val decoder: Decoder[InlineQueryResultLocationContent] = deriveDecoder[InlineQueryResultLocationContent]
  implicit val encoder: Encoder[InlineQueryResultLocationContent] = deriveEncoder[InlineQueryResultLocationContent]
}

/**
 * Content of a venue message to be sent as the result of an inline query.
 *
 * Note: This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
 *
 * @param latitude       Latitude of the venue in degrees
 * @param longitude      Longitude of the venue in degrees
 * @param title          Name of the venue
 * @param address        Address of the venue
 * @param foursquareId   Foursquare identifier of the venue, if known
 * @param foursquareType Foursquare type of the venue, if known.
 *                       For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.
 */
final case class InlineQueryResultVenueContent(latitude: Double,
                                               longitude: Double,
                                               title: String,
                                               address: String,
                                               foursquareId: Option[String] = None,
                                               foursquareType: Option[String] = None)
  extends InlineQueryResultContent

object InlineQueryResultVenueContent {
  implicit val decoder: Decoder[InlineQueryResultVenueContent] = deriveDecoder[InlineQueryResultVenueContent]
  implicit val encoder: Encoder[InlineQueryResultVenueContent] = deriveEncoder[InlineQueryResultVenueContent]
}

/**
 * Content of a contact message to be sent as the result of an inline query.
 *
 * ''Note:''
 * This will only work in Telegram versions released after 9 April, 2016. Older clients will ignore them.
 *
 * @param phoneNumber Contact's phone number
 * @param firstName   Contact's first name
 * @param lastName    Contact's last name
 * @param vcard       Additional data about the contact in the form of a vCard, 0-2048 bytes
 */
final case class InlineQueryResultContactContent(phoneNumber: String,
                                                 firstName: String,
                                                 lastName: Option[String] = None,
                                                 vcard: Option[String] = None)
  extends InlineQueryResultContent

object InlineQueryResultContactContent {
  implicit val decoder: Decoder[InlineQueryResultContactContent] = deriveDecoder[InlineQueryResultContactContent]
  implicit val encoder: Encoder[InlineQueryResultContactContent] = deriveEncoder[InlineQueryResultContactContent]
}
