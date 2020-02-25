package telegram4s

import telegram4s.models._
import telegram4s.models.messages.{LocationMessage, PollMessage, TelegramMessage}

import scala.language.implicitConversions

package object syntax {
  implicit def telegramMethodOps[A](a: A): TelegramMethodOps[A] = new TelegramMethodOps[A](a)
  implicit def chatApi(chat: Chat): ChatApi = new ChatApi(chat)
  implicit def messageApi(message: TelegramMessage): MessageApi = new MessageApi(message)
  implicit def pollApi(message: PollMessage): PollMessageApi = new PollMessageApi(message)
  implicit def liveLocationApi(message: LocationMessage): LiveLocationAPI = new LiveLocationAPI(message)
  implicit def inlineQueryApi(query: InlineQuery): InlineQueryApi = new InlineQueryApi(query)
  implicit def callbackQueryApi(query: CallbackQuery): CallbackQueryAPI = new CallbackQueryAPI(query)
  implicit def preCheckoutQueryApi(query: PreCheckoutQuery): PreCheckoutQueryAPI = new PreCheckoutQueryAPI(query)
  implicit def shippingQueryApi(query: ShippingQuery): ShippingQueryAPI = new ShippingQueryAPI(query)
}
