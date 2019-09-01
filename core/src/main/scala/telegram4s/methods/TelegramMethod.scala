package telegram4s.methods

import io.circe.{Decoder, Encoder}
import telegram4s.marshalling.codecs._
import telegram4s.models.InputFile

class TelegramMethod[Req, Resp](val attachments: Req => List[(String, InputFile)],
                                val name: String)
                               (implicit
                                val encoder: Encoder[Req],
                                val decoder: Decoder[Resp])

object TelegramMethod {
  def apply[Req, Resp](name: String,
                       attachments: Req => List[(String, InputFile)] = (_: Req) => Nil)
                      (implicit encoder: Encoder[Req],
                       decoder: Decoder[Resp]): TelegramMethod[Req, Resp] =
    new TelegramMethod(
      attachments = attachments,
      name = name
    )(
      encoder = encoder.snakeCase,
      decoder = decoder
    )

}