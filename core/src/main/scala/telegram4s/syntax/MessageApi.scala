package telegram4s.syntax

import telegram4s.Telegram4sClient
import telegram4s.methods.messages._
import telegram4s.models.messages.TelegramMessage
import telegram4s.models.outgoing.MessageContent
import telegram4s.models.{Chat, InlineKeyboardMarkup}

/**
 * Telegram API for the any message object.
 * Offers a convenient access to the related Telegram methods.
 */
final class MessageApi(private val message: TelegramMessage) extends AnyVal {

  private def chatId: Long = message.chat.id
  private def messageId: Int = message.messageId

  /**
   * Deletes this message.
   *
   * There are limitations what message can be deleted:
   * - A message can only be deleted if it was sent less than 48 hours ago.
   * - Bots can delete outgoing messages in private chats, groups, and supergroups.
   * - Bots can delete incoming messages in private chats.
   * - Bots granted can_post_messages permissions can delete outgoing messages in channels.
   * - If the bot is an administrator of a group, it can delete any message there.
   * - If the bot has can_delete_messages permission in a supergroup or a channel, it can delete any message there.
   */
  def delete[F[_] : Telegram4sClient]: F[Boolean] = DeleteMessage(chatId, messageId).call

  /**
   * Forwards this message to another chat.
   */
  def forward[F[_] : Telegram4sClient](to: Chat, disableNotification: Option[Boolean] = None): F[TelegramMessage] =
    ForwardMessage(to.id, chatId, messageId, disableNotification).call

  /**
   * Sends new message as a reply to this message.
   */
  def reply[F[_] : Telegram4sClient, M](content: MessageContent[M],
                                        keyboard: Keyboard = Keyboard.Unchanged,
                                        disableNotification: Boolean = false): F[M] =
    message.chat.send(content, Some(messageId), keyboard, disableNotification)

  /**
   * Changes the text of this message.
   *
   * @return On success, if edited message is sent by the bot,
   *         the edited Message is returned, otherwise True is returned.
   */
  def editText[F[_] : Telegram4sClient](text: String): F[Either[Boolean, TelegramMessage]] =
    EditMessageText.direct(chatId, messageId, text = text).call

  /**
   * Changes the reply markup of this message.
   *
   * @return On success, if edited message is sent by the bot,
   *         the edited Message is returned, otherwise True is returned.
   */
  def editReplyMarkup[F[_] : Telegram4sClient](
                                                keyboard: Option[InlineKeyboardMarkup]
                                              ): F[Either[Boolean, TelegramMessage]] =
    EditMessageReplyMarkup.direct(chatId, messageId, replyMarkup = keyboard).call

  /**
   * Changes the caption of this message.
   *
   * @return On success, if edited message is sent by the bot,
   *         the edited Message is returned, otherwise True is returned.
   */
  def editCaption[F[_] : Telegram4sClient](caption: String): F[Either[Boolean, TelegramMessage]] =
    EditMessageCaption.direct(chatId, messageId, caption = Some(caption)).call
}
