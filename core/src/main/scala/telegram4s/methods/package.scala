package telegram4s

import io.circe.Decoder
import telegram4s.marshalling._
import telegram4s.models.ChatAction.ChatAction
import telegram4s.models.Currency.Currency
import telegram4s.models.ParseMode.ParseMode
import telegram4s.models.UpdateType.UpdateType
import telegram4s.models._

package object methods {

  /** Telegram Bot API Response object
   *
   * The response contains a JSON object. If 'ok' equals true, the request was successful and the result of the query can be found in the 'result' field.
   * In case of an unsuccessful request, 'ok' equals false and the error is explained in the 'description'.
   * An Integer 'error_code' field is also returned, but its contents are subject to change in the future.
   *
   * @param ok          Boolean Signals if the request was successful
   * @param result      Optional R Contains the response in a type-safely way
   * @param description Optional String A human-readable description of the result
   * @param errorCode   Optional Integer Error code
   * @tparam R Expected result type
   */
  case class TelegramResponse[R](ok: Boolean,
                                 result: Option[R] = None,
                                 description: Option[String] = None,
                                 errorCode: Option[Int] = None,
                                 parameters: Option[ResponseParameters] = None)

  /** Base type for API requests
   *
   * All queries to the Telegram Bot API must be served over HTTPS and need to be presented
   * in this form: https://api.telegram.org/bot<token>/METHOD_NAME. Like this for example
   *
   * https://api.telegram.org/bot123456:ABC-DEF1234ghIkl-zyx57W2v1u123ew11/getMe
   *
   * We support GET and POST HTTP methods. We support four ways of passing parameters in Bot API requests:
   *  - URL query string
   *  - application/x-www-form-urlencoded
   *  - application/json (except for uploading files)
   *  - multipart/form-data (use to upload files)
   *
   * All methods in the Bot API are case-insensitive.
   * All queries must be made using UTF-8.
   *
   */
  sealed trait ApiRequest {
    type Response

    /** Extract request URL from class name.
     */
    def methodName: String = getClass.getSimpleName.takeWhile('$' != _).capitalize

    implicit def responseDecoder: Decoder[Response]
  }

  /** Base type for JSON-encoded API requests
   *
   *
   * The request will be sent as application/json
   */
  sealed abstract class JsonApiRequest[R](implicit decoder: Decoder[R]) extends ApiRequest {
    override type Response = R
    override implicit def responseDecoder: Decoder[R] = decoder
  }

  /** Base type for multipart API requests (for file uploads)
   *
   * Request will be serialized as multipart/form-data
   */
  sealed abstract class MultipartApiRequest[R](implicit decoder: Decoder[R]) extends ApiRequest {
    override type Response = R
    override implicit def responseDecoder: Decoder[R] = decoder
    def getFiles: List[(String, InputFile)]
  }

  /**
   * Use this method to add a new sticker to a set created by the bot.
   * Returns True on success.
   *
   * @param userId       Integer User identifier of sticker set owner
   * @param name         String	Sticker set name
   * @param pngSticker   InputFile or String Png image with the sticker, must be up to 512 kilobytes in size, dimensions must not exceed 512px, and either width or height must be exactly 512px. Pass a file_id as a String to send a file that already exists on the Telegram servers, pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data. More info on Sending Files »
   * @param emojis       String One or more emoji corresponding to the sticker
   * @param maskPosition MaskPosition Optional Position where the mask should be placed on faces
   */
  case class AddStickerToSet(userId: Int,
                             name: String,
                             pngSticker: InputFile,
                             emojis: String,
                             maskPosition: Option[MaskPosition] = None) extends MultipartApiRequest[Boolean] {
    override def getFiles: List[(String, InputFile)] = List("pngSticker" -> pngSticker)
  }

  /** Use this method to send answers to callback queries sent from inline keyboards.
   *
   * The answer will be displayed to the user as a notification at the top of the chat screen or as an alert.
   * On success, True is returned.
   *
   * Alternatively, the user can be redirected to the specified Game URL.
   * For this option to work, you must first create a game for your bot via BotFather and accept the terms.
   * Otherwise, you may use links like telegram.me/your_bot?start=XXXX that open your bot with a parameter.
   *
   * @param callbackQueryId String Unique identifier for the query to be answered
   * @param text            String Optional Text of the notification. If not specified, nothing will be shown to the user
   * @param showAlert       Boolean Optional If true, an alert will be shown by the client instead of a notification at the top of the chat screen. Defaults to false.
   * @param url             String Optional URL that will be opened by the user's client.
   *                        If you have created a Game and accepted the conditions via @Botfather, specify the URL that opens your game - note that this will only work if the query comes from a callback_game button.
   *                        Otherwise, you may use links like telegram.me/your_bot?start=XXXX that open your bot with a parameter.
   * @param cacheTime       Integer Optional The maximum amount of time in seconds that the result of the callback query may be cached client-side.
   *                        Telegram apps will support caching starting in version 3.14. Defaults to 0.
   *
   */
  case class AnswerCallbackQuery(callbackQueryId: String,
                                 text: Option[String] = None,
                                 showAlert: Option[Boolean] = None,
                                 url: Option[String] = None,
                                 cacheTime: Option[Int] = None) extends JsonApiRequest[Boolean]

  /** Use this method to send answers to an inline query. On success, True is returned.
   * No more than 50 results per query are allowed.
   *
   * @param inlineQueryId     String Unique identifier for the answered query
   * @param results           Array of InlineQueryResult A JSON-serialized array of results for the inline query
   * @param cacheTime         Integer Optional The maximum amount of time in seconds that the result of the inline query may be cached on the server. Defaults to 300.
   * @param isPersonal        Boolean Optional Pass True, if results may be cached on the server side only for the user that sent the query. By default, results may be returned to any user who sends the same query
   * @param nextOffset        String Optional Pass the offset that a client should send in the next query with the same text to receive more results. Pass an empty string if there are no more results or if you don't support pagination. Offset length can't exceed 64 bytes.
   * @param switchPmText      String Optional If passed, clients will display a button with specified text that switches the user to a private chat with the bot and sends the bot a start message with the parameter switch_pm_parameter
   * @param switchPmParameter String Optional Parameter for the start message sent to the bot when user presses the switch buttonExample: An inline bot that sends YouTube videos can ask the user to connect the bot to their YouTube account to adapt search results accordingly. To do this, it displays a 'Connect your YouTube account' button above the results, or even before showing any. The user presses the button, switches to a private chat with the bot and, in doing so, passes a start parameter that instructs the bot to return an oauth link. Once done, the bot can offer a switch_inline button so that the user can easily return to the chat where they wanted to use the bot's inline capabilities.
   */
  case class AnswerInlineQuery(inlineQueryId: String,
                               results: Seq[InlineQueryResult],
                               cacheTime: Option[Int] = None,
                               isPersonal: Option[Boolean] = None,
                               nextOffset: Option[String] = None,
                               switchPmText: Option[String] = None,
                               switchPmParameter: Option[String] = None) extends JsonApiRequest[Boolean]

  /**
   * Once the user has confirmed their payment and shipping details,
   * the Bot API sends the final confirmation in the form of an Update with the field pre_checkout_query.
   * Use this method to respond to such pre-checkout queries.
   * On success, True is returned.
   * '''Note:'''
   * The Bot API must receive an answer within 10 seconds after the pre-checkout query was sent.
   *
   * @param preCheckoutQueryId String Yes Unique identifier for the query to be answered
   * @param ok                 Boolean Yes Specify True if everything is alright (goods are available, etc.)
   *                           and the bot is ready to proceed with the order.
   *                           Use False if there are any problems.
   * @param errorMessage       String Optional Required if ok is False.
   *                           Error message in human readable form that explains the reason for failure to proceed with the checkout
   *                           (e.g.
   *                           "Sorry, somebody just bought the last of our amazing black T-shirts while you were busy filling out your payment details.
   *                           Please choose a different color or garment!").
   *                           Telegram will display this message to the user.
   */
  case class AnswerPreCheckoutQuery(preCheckoutQueryId: String,
                                    ok: Boolean,
                                    errorMessage: Option[String] = None) extends JsonApiRequest[Boolean] {

    require(ok || errorMessage.isDefined, "errorMessage is required if ok is False")
  }

  /**
   * If you sent an invoice requesting a shipping address and the parameter is_flexible was specified,
   * the Bot API will send an Update with a shipping_query field to the bot.
   * Use this method to reply to shipping queries.
   * On success, True is returned.
   *
   * @param shippingQueryId String Yes Unique identifier for the query to be answered
   * @param ok              Boolean Yes Specify True if delivery to the specified address is possible
   *                        and False if there are any problems (for example, if delivery to the specified address is not possible)
   * @param shippingOptions Array of ShippingOption Optional Required if ok is True.
   *                        A JSON-serialized array of available shipping options.
   * @param errorMessage    String Optional Required if ok is False.
   *                        Error message in human readable form that explains why it is impossible to complete the order
   *                        (e.g. "Sorry, delivery to your desired address is unavailable').
   *                        Telegram will display this message to the user.
   */
  case class AnswerShippingQuery(shippingQueryId: String,
                                 ok: Boolean,
                                 shippingOptions: Option[Array[ShippingOption]] = None,
                                 errorMessage: Option[String] = None) extends JsonApiRequest[Boolean] {

    require(!ok || shippingOptions.isDefined, "shippingOptions required if ok is True")
    require(ok || errorMessage.isDefined, "errorMessage required if ok is False")
  }

  /**
   * Use this method to create new sticker set owned by a user.
   * The bot will be able to edit the created sticker set.
   * Returns True on success.
   *
   * @param userId        Integer User identifier of created sticker set owner
   * @param name          String Short name of sticker set, to be used in t.me/addstickers/ URLs (e.g., animals). Can contain only english letters, digits and underscores. Must begin with a letter, can't contain consecutive underscores and must end in “_by_<bot username>”. <bot_username> is case insensitive. 1-64 characters.
   * @param title         String Sticker set title, 1-64 characters
   * @param pngSticker    InputFile or String	Png image with the sticker, must be up to 512 kilobytes in size,
   *                      dimensions must not exceed 512px, and either width or height must be exactly 512px.
   *                      Pass a file_id as a String to send a file that already exists on the Telegram servers,
   *                      pass an HTTP URL as a String for Telegram to get a file from the Internet,
   *                      or upload a new one using multipart/form-data.
   *                      [[https://core.telegram.org/bots/api#sending-files More info on Sending Files]]
   * @param emojis        String One or more emoji corresponding to the sticker
   * @param containsMasks Boolean Optional Pass True, if a set of mask stickers should be created
   * @param maskPosition  MaskPosition Optional Position where the mask should be placed on faces
   */
  case class CreateNewStickerSet(userId: Int,
                                 name: String,
                                 title: String,
                                 pngSticker: InputFile,
                                 emojis: String,
                                 containsMasks: Option[Boolean] = None,
                                 maskPosition: Option[MaskPosition] = None) extends MultipartApiRequest[Boolean] {
    override def getFiles: List[(String, InputFile)] = List("png_sticker" -> pngSticker)
  }


  /**
   * Use this method to delete a chat photo.
   * Photos can't be changed for private chats.
   * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
   * Returns True on success.
   *
   * '''Note:'''
   * In regular groups (non-supergroups), this method will only work if the "All Members Are Admins" setting is off in the target group.
   *
   * @param chatId Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   */
  case class DeleteChatPhoto(chatId: ChatId) extends JsonApiRequest[Boolean]

  /**
   * Use this method to delete a group sticker set from a supergroup.
   * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
   * Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method.
   * Returns True on success.
   *
   * @param chatId Integer or String Yes Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
   */
  case class DeleteChatStickerSet(chatId: ChatId) extends JsonApiRequest[Boolean]

  /** Use this method to delete a message.
   *
   * A message can only be deleted if it was sent less than 48 hours ago.
   * Any such recently sent outgoing message may be deleted.
   * Additionally, if the bot is an administrator in a group chat, it can delete any message.
   * If the bot is an administrator in a supergroup, it can delete messages from any other user
   * and service messages about people joining or leaving the group
   * (other types of service messages may only be removed by the group creator).
   * In channels, bots can only remove their own messages. Returns True on success.
   *
   * @param chatId    Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param messageId Integer Identifier of the message to delete
   */
  case class DeleteMessage(chatId: ChatId, messageId: Int) extends JsonApiRequest[Boolean]

  /**
   * Use this method to delete a sticker from a set created by the bot.
   * Returns True on success.
   *
   * @param sticker String	File identifier of the sticker
   */
  case class DeleteStickerFromSet(sticker: String) extends JsonApiRequest[Boolean]

  /** Use this method to remove webhook integration if you decide to switch back to getUpdates.
   * Returns True on success. Requires no parameters.
   */
  case object DeleteWebhook extends JsonApiRequest[Boolean]

  /** Use this method to edit captions of messages sent by the bot or via the bot (for inline bots).
   * On success, if edited message is sent by the bot, the edited Message is returned, otherwise True is returned.
   *
   * @param chatId          Integer or String No Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param messageId       Integer No Required if inline_message_id is not specified. Unique identifier of the sent message
   * @param inlineMessageId String No Required if chat_id and message_id are not specified. Identifier of the inline message
   * @param caption         String Optional New caption of the message
   * @param parseMode       String Optional Send Markdown or HTML, if you want Telegram apps to show bold, italic,
   *                        fixed-width text or inline URLs in the media caption.
   * @param replyMarkup     InlineKeyboardMarkup Optional A JSON-serialized object for an inline keyboard.
   */
  case class EditMessageCaption(chatId: Option[ChatId] = None,
                                messageId: Option[Int] = None,
                                inlineMessageId: Option[String] = None,
                                caption: Option[String] = None,
                                parseMode: Option[ParseMode] = None,
                                replyMarkup: Option[ReplyMarkup] = None) extends JsonApiRequest[Either[Message, Boolean]] {

    if (inlineMessageId.isEmpty) {
      require(chatId.isDefined, "Required if inlineMessageId is not specified")
      require(messageId.isDefined, "Required if inlineMessageId is not specified")
    }

    if (chatId.isEmpty && messageId.isEmpty)
      require(inlineMessageId.isDefined, "Required if chatId and messageId are not specified")
  }

  /**
   * Use this method to edit live location messages sent by the bot or via the bot (for inline bots).
   * A location can be edited until its live_period expires or editing is explicitly disabled by a call to stopMessageLiveLocation.
   * On success, if the edited message was sent by the bot,
   * the edited Message is returned, otherwise True is returned.
   *
   * @param chatId          Integer or String Optional	Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param messageId       Integer	Optional Required if inline_message_id is not specified. Identifier of the sent message
   * @param inlineMessageId String	Optional Required if chat_id and message_id are not specified. Identifier of the inline message
   * @param latitude        Float number Yes Latitude of new location
   * @param longitude       Float number Yes	Longitude of new location
   * @param replyMarkup     InlineKeyboardMarkup Optional A JSON-serialized object for a new inline keyboard.
   */
  case class EditMessageLiveLocation(chatId: Option[ChatId] = None,
                                     messageId: Option[Int] = None,
                                     inlineMessageId: Option[Int] = None,
                                     latitude: Option[Double] = None,
                                     longitude: Option[Double] = None,
                                     replyMarkup: Option[InlineKeyboardMarkup] = None) extends JsonApiRequest[Either[Message, Boolean]]

  /** Use this method to edit audio, document, photo, or video messages.
   * If a message is a part of a message album, then it can be edited only to a photo or a video.
   * Otherwise, message type can be changed arbitrarily.
   * When inline message is edited, new file can't be uploaded.
   * Use previously uploaded file via its file_id or specify a URL.
   * On success, if the edited message was sent by the bot, the edited Message is returned, otherwise True is returned.
   *
   * @param chatId          Optional Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param messageId       Integer 	Optional Required if inline_message_id is not specified. Identifier of the sent message
   * @param inlineMessageId String Optional 	Required if chat_id and message_id are not specified. Identifier of the inline message
   * @param media           InputMedia 	Yes 	A JSON-serialized object for a new media content of the message
   * @param replyMarkup     InlineKeyboardMarkup Optional A JSON-serialized object for a new inline keyboard.
   */
  case class EditMessageMedia(chatId: Option[ChatId] = None,
                              messageId: Option[Int] = None,
                              inlineMessageId: Option[String] = None,
                              media: InputMedia,
                              replyMarkup: Option[InlineKeyboardMarkup] = None) extends MultipartApiRequest[Either[Boolean, Message]] {

    override def getFiles: List[(String, InputFile)] = {
      media.getFiles
    }
  }


  /** Use this method to edit only the reply markup of messages sent by the bot or via the bot (for inline bots).
   * On success, if edited message is sent by the bot, the edited Message is returned, otherwise True is returned.
   *
   * @param chatId          Integer or String Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param messageId       Integer Required if inline_message_id is not specified. Unique identifier of the sent message
   * @param inlineMessageId String Required if chat_id and message_id are not specified. Identifier of the inline message
   * @param replyMarkup     InlineKeyboardMarkup Optional A JSON-serialized object for an inline keyboard.
   */
  case class EditMessageReplyMarkup(chatId: Option[ChatId] = None,
                                    messageId: Option[Int] = None,
                                    inlineMessageId: Option[String] = None,
                                    replyMarkup: Option[InlineKeyboardMarkup] = None) extends JsonApiRequest[Either[Boolean, Message]] {

    if (inlineMessageId.isEmpty) {
      require(chatId.isDefined, "Required if inlineMessageId is not specified")
      require(messageId.isDefined, "Required if inlineMessageId is not specified")
    }

    if (chatId.isEmpty && messageId.isEmpty)
      require(inlineMessageId.isDefined, "Required if chatId and messageId are not specified")
  }

  /** Use this method to edit text messages sent by the bot or via the bot (for inline bots).
   * On success, if edited message is sent by the bot, the edited Message is returned, otherwise True is returned.
   *
   * @param chatId                Integer or String Required if inline_message_id is not specified. Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param messageId             Integer Required if inline_message_id is not specified. Unique identifier of the sent message
   * @param inlineMessageId       String Required if chat_id and message_id are not specified. Identifier of the inline message
   * @param text                  String New text of the message
   * @param parseMode             String Optional Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in your bot's message.
   * @param disableWebPagePreview Boolean Optional Disables link previews for links in this message
   * @param replyMarkup           InlineKeyboardMarkup Optional A JSON-serialized object for an inline keyboard.
   */
  case class EditMessageText(chatId: Option[ChatId] = None,
                             messageId: Option[Int] = None,
                             inlineMessageId: Option[String] = None,
                             text: String,
                             parseMode: Option[ParseMode] = None,
                             disableWebPagePreview: Option[Boolean] = None,
                             replyMarkup: Option[ReplyMarkup] = None) extends JsonApiRequest[Either[Boolean, Message]] {
    if (inlineMessageId.isEmpty) {
      require(chatId.isDefined, "Required if inlineMessageId is not specified")
      require(messageId.isDefined, "Required if inlineMessageId is not specified")
    }

    if (chatId.isEmpty && messageId.isEmpty)
      require(inlineMessageId.isDefined, "Required if chatId and messageId are not specified")
  }

  /**
   * Use this method to export an invite link to a supergroup or a channel.
   * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
   * Returns exported invite link as String on success.
   *
   * @param chatId Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   */
  case class ExportChatInviteLink(chatId: ChatId) extends JsonApiRequest[String]

  /** Use this method to forward messages of any kind. On success, the sent Message is returned.
   *
   * @param chatId              Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param fromChatId          Integer or String Unique identifier for the chat where the original message was sent (or channel username in the format @channelusername)
   * @param disableNotification Boolean Optional Sends the message silently. iOS users will not receive a notification, Android users will receive a notification with no sound.
   * @param messageId           Integer Unique message identifier
   */
  case class ForwardMessage(chatId: ChatId,
                            fromChatId: ChatId,
                            disableNotification: Option[Boolean] = None,
                            messageId: Int) extends JsonApiRequest[Message]

  /** Use this method to get up to date information about the chat (current name of the user for one-on-one conversations, current username of a user, group or channel, etc.).
   * Returns a Chat object on success.
   *
   * @param chatId Integer or String Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
   */
  case class GetChat(chatId: ChatId) extends JsonApiRequest[Chat]

  /** Use this method to get a list of administrators in a chat.
   * On success, returns an Array of ChatMember objects that contains information about all chat administrators except other bots.
   * If the chat is a group or a supergroup and no administrators were appointed, only the creator will be returned.
   *
   * @param chatId Integer or String Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
   */
  case class GetChatAdministrators(chatId: ChatId) extends JsonApiRequest[Seq[ChatMember]]

  /** Use this method to get information about a member of a chat. Returns a ChatMember object on success.
   *
   * @param chatId Integer or String Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
   * @param userId Integer Unique identifier of the target user
   */
  case class GetChatMember(chatId: ChatId, userId: Int) extends JsonApiRequest[ChatMember]

  /** Use this method to get the number of members in a chat. Returns Int on success.
   *
   * @param chatId Integer or String Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
   */
  case class GetChatMembersCount(chatId: ChatId) extends JsonApiRequest[Int]

  /** Use this method to get basic info about a file and prepare it for downloading.
   * For the moment, bots can download files of up to 20MB in size. On success, a File object is returned.
   * The file can then be downloaded via the link https://api.telegram.org/file/bot<token>/<file_path>,
   * where <file_path> is taken from the response.
   * It is guaranteed that the link will be valid for at least 1 hour.
   * When the link expires, a new one can be requested by calling getFile again.
   *
   * @param fileId String File identifier to get info about
   */
  case class GetFile(fileId: String) extends JsonApiRequest[File]

  /** Use this method to get data for high score tables.
   * Will return the score of the specified user and several of his neighbors in a game.
   * On success, returns an Array of GameHighScore objects.
   *
   * This method will currently return scores for the target user, plus two of his closest neighbors on each side.
   * Will also return the top three users if the user and his neighbors are not among them.
   * Please note that this behavior is subject to change.
   *
   * @param userId          Integer Yes Target user id
   * @param chatId          Integer or String Optional Required if inline_message_id is not specified. Unique identifier for the target chat (or username of the target channel in the format @channelusername)
   * @param messageId       Integer Optional Required if inline_message_id is not specified. Unique identifier of the sent message
   * @param inlineMessageId String Optional Required if chat_id and message_id are not specified. Identifier of the inline message
   */
  case class GetGameHighScores(userId: Int,
                               chatId: Option[ChatId] = None,
                               messageId: Option[Int] = None,
                               inlineMessageId: Option[String] = None) extends JsonApiRequest[Seq[GameHighScore]]

  /** A simple method for testing your bot's auth token. Requires no parameters.
   * Returns basic information about the bot in form of a User object.
   */
  case object GetMe extends JsonApiRequest[User]

  /**
   * Use this method to get a sticker set.
   * On success, a StickerSet object is returned.
   *
   * @param name String Name of the sticker set
   */
  case class GetStickerSet(name: String) extends JsonApiRequest[StickerSet]

  /** Use this method to receive incoming updates using long polling (wiki). An Array of Update objects is returned.
   *
   * @param offset         Integer Optional Identifier of the first update to be returned. Must be greater by one than the highest among the identifiers of previously received updates. By default, updates starting with the earliest unconfirmed update are returned. An update is considered confirmed as soon as getUpdates is called with an offset higher than its update_id. The negative offset can be specified to retrieve updates starting from -offset update from the end of the updates queue. All previous updates will forgotten.
   * @param limit          Integer Optional Limits the number of updates to be retrieved. Values between 1-100 are accepted. Defaults to 100.
   * @param timeout        Integer Optional Timeout in seconds for long polling. Defaults to 0, i.e. usual short polling
   * @param allowedUpdates Array of String Optional List the types of updates you want your bot to receive.
   *                       For example, specify [“message”, “edited_channel_post”, “callback_query”] to only receive updates of these types.
   *                       See Update for a complete list of available update types.
   *                       Specify an empty list to receive all updates regardless of type (default).
   *                       If not specified, the previous setting will be used.
   *                       Please note that this parameter doesn't affect updates created before the call to the getUpdates, so unwanted updates may be received for a short period of time.
   *
   *                       Notes
   *   1. This method will not work if an outgoing webhook is set up.
   *   2. In order to avoid getting duplicate updates, recalculate offset after each server response.
   */
  case class GetUpdates(offset: Option[Long] = None,
                        limit: Option[Int] = None,
                        timeout: Option[Int] = None,
                        allowedUpdates: Option[Seq[UpdateType]] = None) extends JsonApiRequest[Seq[Update]]

  /** Use this method to get a list of profile pictures for a user. Returns a UserProfilePhotos object.
   *
   * @param userId Integer Unique identifier of the target user
   * @param offset Integer Optional Sequential number of the first photo to be returned. By default, all photos are returned.
   * @param limit  Integer Optional Limits the number of photos to be retrieved. Values between 1-100 are accepted. Defaults to 100.
   */
  case class GetUserProfilePhotos(userId: Int,
                                  offset: Option[Int] = None,
                                  limit: Option[Int] = None) extends JsonApiRequest[UserProfilePhotos]

  /** Use this method to get current webhook status.
   * Requires no parameters. On success, returns a WebhookInfo object.
   * If the bot is using getUpdates, will return an object with the url field empty.
   */
  case object GetWebhookInfo extends JsonApiRequest[WebhookInfo]

  /**
   * Use this method to kick a user from a group, a supergroup or a channel.
   * In the case of supergroups and channels, the user will not be able to return to the group on their own using invite links, etc., unless unbanned first.
   * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
   * Returns True on success.
   *
   * '''Note:'''
   * In regular groups (non-supergroups), this method will only work if the "All Members Are Admins" setting is off in the target group.
   * Otherwise members may only be removed by the group's creator or by the member that added them.
   *
   * @param chatId    Integer or String Unique identifier for the target group or username of the target supergroup (in the format @supergroupusername)
   * @param userId    Integer Unique identifier of the target user
   * @param untilDate Integer Optional Date when the user will be unbanned, unix time.
   *                  If user is banned for more than 366 days or less than 30 seconds from the current time they are considered to be banned forever
   */
  case class KickChatMember(chatId: ChatId,
                            userId: Int,
                            untilDate: Option[Int] = None) extends JsonApiRequest[Boolean]

  /** Use this method for your bot to leave a group, supergroup or channel. Returns True on success.
   *
   * @param chatId Integer or String Unique identifier for the target chat or username of the target supergroup or channel (in the format @channelusername)
   */
  case class LeaveChat(chatId: ChatId) extends JsonApiRequest[Boolean]

  /**
   * Use this method to pin a message in a supergroup.
   * The bot must be an administrator in the chat for this to work and must have the ‘can_pin_messages’ admin right in
   * the supergroup or ‘can_edit_messages’ admin right in the channel.
   * Returns True on success.
   *
   * @param chatId              Integer or String	Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param messageId           Integer	Identifier of a message to pin
   * @param disableNotification Boolean	Optional Pass True, if it is not necessary to send a notification to all chat members about the new pinned message.
   *                            Notifications are always disabled in channels.
   */
  case class PinChatMessage(chatId: ChatId,
                            messageId: Int,
                            disableNotification: Option[Boolean] = None) extends JsonApiRequest[Boolean]

  /**
   * Use this method to promote or demote a user in a supergroup or a channel.
   * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
   * Pass False for all boolean parameters to demote a user.
   * Returns True on success.
   *
   * @param chatId             Integer or String Unique identifier for the target chat or username of the target channel
   *                           (in the format @channelusername)
   * @param userId             Integer Unique identifier of the target user
   * @param canChangeInfo      Boolean Optional Pass True, if the administrator can change chat title, photo and other settings
   * @param canPostMessages    Boolean Optional Pass True, if the administrator can create channel posts, channels only
   * @param canEditMessages    Boolean Optional Pass True, if the administrator can edit messages of other users, channels only
   * @param canDeleteMessages  Boolean Optional Pass True, if the administrator can delete messages of other users
   * @param canInviteUsers     Boolean Optional Pass True, if the administrator can invite new users to the chat
   * @param canRestrictMembers Boolean Optional Pass True, if the administrator can restrict, ban or unban chat members
   * @param canPinMessages     Boolean Optional Pass True, if the administrator can pin messages, supergroups only
   * @param canPromoteMembers  Boolean Optional Pass True, if the administrator can add new administrators with a subset
   *                           of his own privileges or demote administrators that he has promoted,
   *                           directly or indirectly (promoted by administrators that were appointed by him)
   */
  case class PromoteChatMember(chatId: ChatId,
                               userId: Int,
                               canChangeInfo: Option[Boolean] = None,
                               canPostMessages: Option[Boolean] = None,
                               canEditMessages: Option[Boolean] = None,
                               canDeleteMessages: Option[Boolean] = None,
                               canInviteUsers: Option[Boolean] = None,
                               canRestrictMembers: Option[Boolean] = None,
                               canPinMessages: Option[Boolean] = None,
                               canPromoteMembers: Option[Boolean] = None) extends JsonApiRequest[Boolean]

  /**
   * Use this method to restrict a user in a supergroup.
   * The bot must be an administrator in the supergroup for this to work and must have the appropriate admin rights.
   * Pass True for all boolean parameters to lift restrictions from a user.
   * Returns True on success.
   *
   * @param chatId                Integer or String	Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
   * @param userId                Integer	Yes	Unique identifier of the target user
   * @param permissions           ChatPermissions New user permissions
   * @param untilDate             Integer Optional Date when restrictions will be lifted for the user, unix time.
   *                              If user is restricted for more than 366 days or less than 30 seconds from the current time, they are considered to be restricted forever
   * @param canSendMessages       Boolean	Optional Pass True, if the user can send text messages, contacts, locations and venues
   * @param canSendMediaMessages  Boolean	Optional Pass True, if the user can send audios, documents, photos, videos, video notes and voice notes, implies can_send_messages
   * @param canSendOtherMessages  Boolean	Optional Pass True, if the user can send animations, games, stickers and use inline bots, implies can_send_media_messages
   * @param canAddWebPagePreviews Boolean Optional Pass True, if the user may add web page previews to their messages, implies can_send_media_messages
   */
  case class RestrictChatMember(chatId: ChatId,
                                userId: Int,
                                permissions: Option[ChatPermissions] = None,
                                untilDate: Option[Int] = None,
                                @deprecated canSendMessages: Option[Boolean] = None,
                                @deprecated canSendMediaMessages: Option[Boolean] = None,
                                @deprecated canSendOtherMessages: Option[Boolean] = None,
                                @deprecated canAddWebPagePreviews: Option[Boolean] = None) extends JsonApiRequest[Boolean]

  /** Use this method to send animation files (GIF or H.264/MPEG-4 AVC video without sound).
   * On success, the sent Message is returned.
   * Bots can currently send animation files of up to 50 MB in size, this limit may be changed in the future.
   *
   * @param chatId              Integer or String 	Yes 	Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param animation           InputFile or String 	Yes 	Animation to send. Pass a file_id as String to send an animation that exists on the Telegram servers (recommended), pass an HTTP URL as a String for Telegram to get an animation from the Internet, or upload a new animation using multipart/form-data. More info on Sending Files »
   * @param duration            Integer 	Optional 	Duration of sent animation in seconds
   * @param width               Integer 	Optional 	Animation width
   * @param height              Integer 	Optional 	Animation height
   * @param thumb               InputFile or String 	Optional 	Thumbnail of the file sent. The thumbnail should be in JPEG format and less than 200 kB in size. A thumbnail‘s width and height should not exceed 90. Ignored if the file is not uploaded using multipart/form-data. Thumbnails can’t be reused and can be only uploaded as a new file, so you can pass “attach://<file_attach_name>” if the thumbnail was uploaded using multipart/form-data under <file_attach_name>. More info on Sending Files »
   * @param caption             String 	Optional 	Animation caption (may also be used when resending animation by file_id), 0-200 characters
   * @param parseMode           String 	Optional 	Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in the media caption.
   * @param disableNotification Boolean 	Optional 	Sends the message silently. Users will receive a notification with no sound.
   * @param replyToMessageId    Integer 	Optional 	If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup or ReplyKeyboardMarkup or ReplyKeyboardRemove or ForceReply 	Optional 	Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
   */
  case class SendAnimation(chatId: ChatId,
                           animation: InputFile,
                           duration: Option[Int] = None,
                           width: Option[Int] = None,
                           height: Option[Int] = None,
                           thumb: Option[InputFile] = None,
                           caption: Option[String] = None,
                           parseMode: Option[ParseMode] = None,
                           disableNotification: Option[Boolean] = None,
                           replyToMessageId: Option[Int],
                           replyMarkup: Option[ReplyMarkup] = None) extends MultipartApiRequest[Message] {
    override def getFiles: List[(String, InputFile)] = {
      List("animation" -> animation) ++ thumb.map(t => "thumb" -> t).toList
    }
  }

  /** Use this method to send audio files, if you want Telegram clients to display them in the music player.
   * Your audio must be in the .mp3 format.
   * On success, the sent Message is returned.
   * Bots can currently send audio files of up to 50 MB in size, this limit may be changed in the future.
   *
   * For sending voice messages, use the sendVoice method instead.
   *
   * @param chatId              Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param audio               InputFile or String Audio file to send. Audio file to send.
   *                            Pass a file_id as String to send an audio file that exists on the Telegram servers (recommended),
   *                            pass an HTTP URL as a String for Telegram to get an audio file from the Internet, or upload a new one using multipart/form-data.
   * @param caption             String Optional Audio caption, 0-200 characters
   * @param parseMode           String Optional Send Markdown or HTML, if you want Telegram apps to show bold, italic,
   *                            fixed-width text or inline URLs in the media caption.
   * @param duration            Integer Optional Duration of the audio in seconds
   * @param performer           String Optional Performer
   * @param title               String Optional Track name
   * @param disableNotification Boolean Optional Sends the message silently. iOS users will not receive a notification,
   *                            Android users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup or ReplyKeyboardMarkup or ReplyKeyboardHide or ForceReply Optional Additional interface options.
   *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
   *                            instructions to hide reply keyboard or to force a reply from the user.
   */
  case class SendAudio(chatId: ChatId,
                       audio: InputFile,
                       duration: Option[Int] = None,
                       caption: Option[String] = None,
                       parseMode: Option[ParseMode] = None,
                       performer: Option[String] = None,
                       title: Option[String] = None,
                       disableNotification: Option[Boolean] = None,
                       replyToMessageId: Option[Long] = None,
                       replyMarkup: Option[ReplyMarkup] = None) extends MultipartApiRequest[Message] {
    override def getFiles: List[(String, InputFile)] = List("audio" -> audio)
  }

  /** Use this method when you need to tell the user that something is happening on the bot's side.
   * The status is set for 5 seconds or less (when a message arrives from your bot, Telegram clients clear its typing status).
   *
   * Example: The ImageBot needs some time to process a request and upload the image.
   * Instead of sending a text message along the lines of "Retrieving image, please wait...", the bot may use sendChatAction with action = upload_photo.
   * The user will see a "sending photo" status for the bot.
   * We only recommend using this method when a response from the bot will take a noticeable amount of time to arrive.
   *
   * @param chatId Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param action String Type of action to broadcast.
   *               Choose one, depending on what the user is about to receive:
   *               typing for text messages, upload_photo for photos, record_video or upload_video for videos, record_audio or upload_audio for audio files, upload_document for general files, find_location for location data.
   */
  case class SendChatAction(chatId: ChatId, action: ChatAction) extends JsonApiRequest[Boolean]

  /** Use this method to send phone contacts.
   * On success, the sent Message is returned.
   *
   * @param chatId              Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param phoneNumber         String Contact's phone number
   * @param firstName           String Contact's first name
   * @param lastName            String Optional Contact's last name
   * @param vcard               String Optional Additional data about the contact in the form of a vCard, 0-2048 bytes
   * @param disableNotification Boolean Optional Sends the message silently.
   *                            iOS users will not receive a notification, Android users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup or ReplyKeyboardMarkup or ReplyKeyboardHide or ForceReply Optional Additional interface options.
   *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
   *                            instructions to hide keyboard or to force a reply from the user.
   *
   */
  case class SendContact(chatId: ChatId,
                         phoneNumber: String,
                         firstName: String,
                         lastName: Option[String] = None,
                         vcard: Option[String] = None,
                         disableNotification: Option[Boolean] = None,
                         replyToMessageId: Option[Int] = None,
                         replyMarkup: Option[ReplyMarkup] = None) extends JsonApiRequest[Message]


  /** Use this method to send general files. On success, the sent Message is returned.
   * Bots can currently send files of any type of up to 50 MB in size, this limit may be changed in the future.
   *
   * @param chatId              Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param document            InputFile or String File to send.
   *                            Pass a file_id as String to send a file that exists on the Telegram servers (recommended),
   *                            pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data.
   * @param caption             String Optional Document caption (may also be used when resending documents by file_id), 0-200 characters
   * @param parseMode           String Optional Send Markdown or HTML, if you want Telegram apps to show bold, italic,
   *                            fixed-width text or inline URLs in the media caption.
   * @param disableNotification Boolean Optional Sends the message silently. iOS users will not receive a notification,
   *                            Android users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup or ReplyKeyboardMarkup or ReplyKeyboardHide or ForceReply Optional Additional interface options.
   *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
   *                            instructions to hide reply keyboard or to force a reply from the user.
   */
  case class SendDocument(chatId: ChatId,
                          document: InputFile,
                          caption: Option[String] = None,
                          parseMode: Option[ParseMode] = None,
                          disableNotification: Option[Boolean] = None,
                          replyToMessageId: Option[Long] = None,
                          replyMarkup: Option[ReplyMarkup] = None) extends MultipartApiRequest[Message] {
    override def getFiles: List[(String, InputFile)] = List("document" -> document)
  }

  /** Use this method to send a game.
   * On success, the sent Message is returned.
   *
   * @param chatId              Integer	Yes	Unique identifier for the target chat
   * @param gameShortName       String Short name of the game, serves as the unique identifier for the game. Set up your games via Botfather.
   * @param disableNotification Boolean Optional Sends the message silently.
   *                            iOS users will not receive a notification, Android users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup Optional A JSON-serialized object for an inline keyboard.
   *                            If empty, one 'Play game_title' button will be shown.
   *                            If not empty, the first button must launch the game.
   */
  case class SendGame(chatId: Long,
                      gameShortName: String,
                      disableNotification: Option[Boolean] = None,
                      replyToMessageId: Option[Int] = None,
                      replyMarkup: Option[ReplyMarkup] = None) extends JsonApiRequest[Message]

  /**
   * Use this method to send invoices.
   * On success, the sent Message is returned.
   *
   * @param chatId              Integer Yes Unique identifier for the target private chat
   * @param title               String Yes Product name
   * @param description         String Yes Product description
   * @param payload             String Yes Bot-defined invoice payload, 1-128 bytes. This will not be displayed to the user, use for your internal processes.
   * @param providerToken       String Yes Payments provider token, obtained via Botfather
   * @param startParameter      String Yes Unique deep-linking parameter that can be used to generate this invoice when used as a start parameter
   * @param currency            String Yes Three-letter ISO 4217 currency code, see more on currencies
   * @param prices              Array of LabeledPrice Yes Price breakdown, a list of components (e.g. product price, tax, discount, delivery cost, delivery tax, bonus, etc.)
   * @param providerData        String Optional JSON-encoded data about the invoice, which will be shared with the payment provider.
   *                            A detailed description of required fields should be provided by the payment provider.
   * @param photoUrl            String Optional URL of the product photo for the invoice.
   *                            Can be a photo of the goods or a marketing image for a service.
   *                            People like it better when they see what they are paying for.
   * @param photoSize           Integer Optional Photo size
   * @param photoWidth          Integer Optional Photo width
   * @param photoHeight         Integer Optional Photo height
   * @param needName            Bool Optional Pass True, if you require the user's full name to complete the order
   * @param needPhoneNumber     Boolean Optional Pass True, if you require the user's phone number to complete the order
   * @param needEmail           Bool Optional Pass True, if you require the user's email to complete the order
   * @param needShippingAddress Boolean Optional Pass True, if you require the user's shipping address to complete the order
   * @param isFlexible          Boolean Optional Pass True, if the final price depends on the shipping method
   * @param disableNotification Boolean Optional Sends the message silently. Users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup Optional A JSON-serialized object for an inline keyboard.
   *                            If empty, one 'Pay total price' button will be shown.
   *                            If not empty, the first button must be a Pay button.
   */
  case class SendInvoice(chatId: Long,
                         title: String,
                         description: String,
                         payload: String,
                         providerToken: String,
                         startParameter: String,
                         currency: Currency,
                         prices: Array[LabeledPrice],
                         providerData: Option[String] = None,
                         photoUrl: Option[String] = None,
                         photoSize: Option[Int] = None,
                         photoWidth: Option[Int] = None,
                         photoHeight: Option[Int] = None,
                         needName: Option[Boolean] = None,
                         needPhoneNumber: Option[Boolean] = None,
                         needEmail: Option[Boolean] = None,
                         needShippingAddress: Option[Boolean] = None,
                         isFlexible: Option[Boolean] = None,
                         disableNotification: Option[Int] = None,
                         replyToMessageId: Option[Long] = None,
                         replyMarkup: Option[InlineKeyboardMarkup] = None) extends JsonApiRequest[Message]

  /** Use this method to send point on the map.
   * On success, the sent Message is returned.
   *
   * @param chatId              Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param latitude            Float number Latitude of location
   * @param longitude           Float number Longitude of location
   * @param livePeriod          Integer Optional Period in seconds for which the location will be updated
   *                            (see Live Locations, should be between 60 and 86400.
   * @param disableNotification Boolean Optional Sends the message silently.
   *                            iOS users will not receive a notification, Android users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup or ReplyKeyboardMarkup or ReplyKeyboardHide or ForceReply Optional Additional interface options.
   *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
   *                            instructions to hide reply keyboard or to force a reply from the user.
   */
  case class SendLocation(chatId: ChatId,
                          latitude: Double,
                          longitude: Double,
                          livePeriod: Option[Int] = None,
                          disableNotification: Option[Boolean] = None,
                          replyToMessageId: Option[Int] = None,
                          replyMarkup: Option[ReplyMarkup] = None) extends JsonApiRequest[Message]

  /**
   * Use this method to send a group of photos or videos as an album.
   * On success, an array of the sent Messages is returned.
   *
   * @param chatId              Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param media               Array of InputMedia A JSON-serialized array describing photos and videos to be sent, must include 2–10 items
   * @param disableNotification Boolean Optional Sends the messages silently. Users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the messages are a reply, ID of the original message
   */
  case class SendMediaGroup(chatId: ChatId,
                            media: Array[InputMedia],
                            disableNotification: Option[Boolean] = None,
                            replyToMessageId: Option[Int] = None) extends MultipartApiRequest[Array[Message]] {

    override def getFiles: List[(String, InputFile)] = {
      val attachPrefix = "attach://"
      media.toList.flatMap {
        case photo: InputMediaPhoto         => photo.photo.map(photo.media.stripPrefix(attachPrefix) -> _)
        case video: InputMediaVideo         => video.video.map(video.media.stripPrefix(attachPrefix) -> _)
        case audio: InputMediaAudio         => audio.audio.map(audio.media.stripPrefix(attachPrefix) -> _)
        case document: InputMediaDocument   => document.document.map(document.media.stripPrefix(attachPrefix) -> _)
        case animation: InputMediaAnimation => animation.animation.map(animation.media.stripPrefix(attachPrefix) -> _)
      }
    }
  }

  /** Use this method to send text messages.
   * On success, the sent Message is returned.
   *
   * ==Formatting options==
   * The Bot API supports basic formatting for messages. You can use bold and italic text, as well as inline links and pre-formatted code in your bots' messages.
   * Telegram clients will render them accordingly. You can use either markdown-style or HTML-style formatting.
   * Note that Telegram clients will display an alert to the user before opening an inline link ('Open this link?' together with the full URL).
   *
   * ===Markdown style===
   * To use this mode, pass Markdown in the parse_mode field when using sendMessage. Use the following syntax in your message:
   * *bold text*
   * _italic text_
   * [text](URL)
   * `inline fixed-width code`
   * ```pre-formatted fixed-width code block```
   *
   * ===HTML style===
   * To use this mode, pass HTML in the parse_mode field when using sendMessage. The following tags are currently supported:
   * <b>bold</b>, <strong>bold</strong>
   * <i>italic</i>, <em>italic</em>
   * <a href="URL">inline URL</a>
   * <code>inline fixed-width code</code>
   * <pre>pre-formatted fixed-width code block</pre>
   *
   * '''Please note:'''
   *
   * Only the tags mentioned above are currently supported.
   * Tags must not be nested.
   * All <, > and & symbols that are not a part of a tag or an HTML entity must be replaced with the corresponding HTML entities (< with &lt;, > with &gt; and & with &amp;).
   * All numerical HTML entities are supported.
   * The API currently supports only the following named HTML entities: &lt;, &gt;, &amp; and &quot;.
   *
   * @param chatId                Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param text                  String Text of the message to be sent
   * @param parseMode             String Optional Send Markdown or HTML, if you want Telegram apps to show bold, italic, fixed-width text or inline URLs in your bot's message.
   * @param disableWebPagePreview Boolean Optional Disables link previews for links in this message
   * @param disableNotification   Boolean Optional Sends the message silently.
   *                              iOS users will not receive a notification, Android users will receive a notification with no sound.
   * @param replyToMessageId      Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup           InlineKeyboardMarkup or ReplyKeyboardMarkup or ReplyKeyboardHide or ForceReply Optional Additional interface options.
   *                              A JSON-serialized object for an inline keyboard, custom reply keyboard,
   *                              instructions to hide reply keyboard or to force a reply from the user.
   */
  case class SendMessage(chatId: ChatId,
                         text: String,
                         parseMode: Option[ParseMode] = None,
                         disableWebPagePreview: Option[Boolean] = None,
                         disableNotification: Option[Boolean] = None,
                         replyToMessageId: Option[Int] = None,
                         replyMarkup: Option[ReplyMarkup] = None) extends JsonApiRequest[Message]

  /** Use this method to send photos.
   * On success, the sent Message is returned.
   *
   * @param chatId              Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param photo               InputFile or String Photo to send.
   *                            Pass a file_id as String to send a photo that exists on the Telegram servers (recommended),
   *                            pass an HTTP URL as a String for Telegram to get a photo from the Internet, or upload a new photo using multipart/form-data.
   * @param caption             String Optional Photo caption (may also be used when resending photos by file_id), 0-200 characters
   * @param parseMode           String Optional Send Markdown or HTML, if you want Telegram apps to show bold, italic,
   *                            fixed-width text or inline URLs in the media caption.
   * @param disableNotification Boolean Optional Sends the message silently.
   *                            iOS users will not receive a notification, Android users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup or ReplyKeyboardMarkup or ReplyKeyboardHide or ForceReply Optional Additional interface options.
   *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
   *                            instructions to hide reply keyboard or to force a reply from the user.
   */
  case class SendPhoto(chatId: ChatId,
                       photo: InputFile,
                       caption: Option[String] = None,
                       parseMode: Option[ParseMode] = None,
                       disableNotification: Option[Boolean] = None,
                       replyToMessageId: Option[Int] = None,
                       replyMarkup: Option[ReplyMarkup] = None) extends MultipartApiRequest[Message] {
    override def getFiles: List[(String, InputFile)] = List("photo" -> photo)
  }

  /**
   * Use this method to send a native poll.
   * A native poll can't be sent to a private chat. On success, the sent Message is returned.
   *
   * @param chatId              Unique identifier for the target chat or username of the target channel (in the format @channelusername). A native poll can't be sent to a private chat.
   * @param question            Poll question, 1-255 characters
   * @param options             List of answer options, 2-10 strings 1-100 characters each
   * @param disableNotification Sends the message silently. Users will receive a notification with no sound.
   * @param replyToMessageId    If the message is a reply, ID of the original message
   * @param replyMarkup         Additional interface options. A JSON-serialized object for an inline keyboard, custom reply keyboard, instructions to remove reply keyboard or to force a reply from the user.
   */
  case class SendPoll(chatId: ChatId,
                      question: String,
                      options: Array[String],
                      disableNotification: Option[Boolean] = None,
                      replyToMessageId: Option[Int] = None,
                      replyMarkup: Option[ReplyMarkup] = None) extends JsonApiRequest[Message]

  /**
   * Use this method to send static .WEBP or animated .TGS stickers. On success, the sent Message is returned.
   *
   * @param chatId              Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param sticker             InputFile or String Sticker to send.
   *                            Pass a file_id as String to send a file that exists on the Telegram servers (recommended),
   *                            pass an HTTP URL as a String for Telegram to get a .webp file from the Internet, or upload a new one using multipart/form-data.
   * @param disableNotification Boolean Optional Sends the message silently.
   *                            iOS users will not receive a notification, Android users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup or ReplyKeyboardMarkup or ReplyKeyboardHide or ForceReply Optional Additional interface options.
   *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
   *                            instructions to hide reply keyboard or to force a reply from the user.
   */
  case class SendSticker(chatId: ChatId,
                         sticker: InputFile,
                         disableNotification: Option[Boolean] = None,
                         replyToMessageId: Option[Int] = None,
                         replyMarkup: Option[ReplyMarkup] = None) extends MultipartApiRequest[Message] {
    override def getFiles: List[(String, InputFile)] = List("sticker" -> sticker)
  }

  /** Use this method to send information about a venue. On success, the sent Message is returned.
   *
   * @param chatId              Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param latitude            Float number Latitude of the venue
   * @param longitude           Float number Longitude of the venue
   * @param title               String Name of the venue
   * @param address             String Address of the venue
   * @param foursquareId        String Optional Foursquare identifier of the venue
   * @param foursquareType      String Optional. Foursquare type of the venue, if known. (For example, “arts_entertainment/default”, “arts_entertainment/aquarium” or “food/icecream”.)
   * @param disableNotification Boolean Optional Sends the message silently.
   *                            iOS users will not receive a notification, Android users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup or ReplyKeyboardMarkup or ReplyKeyboardHide or ForceReply Optional Additional interface options.
   *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
   *                            instructions to hide reply keyboard or to force a reply from the user.
   */
  case class SendVenue(chatId: ChatId,
                       latitude: Double,
                       longitude: Double,
                       title: String,
                       address: String,
                       foursquareId: Option[String] = None,
                       foursquareType: Option[String] = None,
                       duration: Option[String] = None,
                       disableNotification: Option[Boolean] = None,
                       replyToMessageId: Option[Int] = None,
                       replyMarkup: Option[ReplyMarkup] = None) extends JsonApiRequest[Message]

  /** Use this method to send video files, Telegram clients support mp4 videos (other formats may be sent as Document).
   * On success, the sent Message is returned.
   * Bots can currently send video files of up to 50 MB in size, this limit may be changed in the future.
   *
   * @param chatId              Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param video               InputFile or String Video to send. Pass a file_id as String to send a video that exists on the Telegram servers (recommended),
   *                            pass an HTTP URL as a String for Telegram to get a video from the Internet, or upload a new video using multipart/form-data.
   * @param duration            Integer Optional Duration of sent video in seconds
   * @param width               Integer Optional Video width
   * @param height              Integer Optional Video height
   * @param caption             String Optional Video caption (may also be used when resending videos by file_id), 0-200 characters
   * @param parseMode           String Optional Send Markdown or HTML, if you want Telegram apps to show bold, italic,
   *                            fixed-width text or inline URLs in the media caption.
   * @param supportsStreaming   Boolean Optional Pass True, if the uploaded video is suitable for streaming
   * @param disableNotification Boolean Optional Sends the message silently.
   *                            iOS users will not receive a notification, Android users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup or ReplyKeyboardMarkup or ReplyKeyboardHide or ForceReply Optional Additional interface options.
   *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
   *                            instructions to hide reply keyboard or to force a reply from the user.
   */
  case class SendVideo(chatId: ChatId,
                       video: InputFile,
                       duration: Option[Int] = None,
                       width: Option[Int] = None,
                       height: Option[Int] = None,
                       caption: Option[String] = None,
                       parseMode: Option[ParseMode] = None,
                       supportsStreaming: Option[Boolean] = None,
                       disableNotification: Option[Boolean] = None,
                       replyToMessageId: Option[Long] = None,
                       replyMarkup: Option[ReplyMarkup] = None) extends MultipartApiRequest[Message] {
    override def getFiles: List[(String, InputFile)] = List("video" -> video)
  }

  /**
   * As of v.4.0, Telegram clients support rounded square mp4 videos of up to 1 minute long.
   * Use this method to send video messages.
   * On success, the sent Message is returned.
   *
   * @param chatId              Integer or String Yes Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param videoNote           InputFile or String Yes Video note to send.
   *                            Pass a file_id as String to send a video note that exists on the Telegram servers (recommended)
   *                            or upload a new video using multipart/form-data. More info on Sending Files ».
   *                            Sending video notes by a URL is currently unsupported
   * @param duration            Integer Optional Duration of sent video in seconds
   * @param length              Integer Optional Video width and height
   * @param disableNotification Boolean Optional Sends the message silently.
   *                            iOS users will not receive a notification, Android users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup or ReplyKeyboardMarkup or ReplyKeyboardRemove or ForceReply Optional
   */
  case class SendVideoNote(chatId: ChatId,
                           videoNote: InputFile,
                           duration: Option[Int] = None,
                           length: Option[Int] = None,
                           disableNotification: Option[Boolean] = None,
                           replyToMessageId: Option[Int] = None,
                           replyMarkup: Option[ReplyMarkup] = None) extends MultipartApiRequest[Message] {
    override def getFiles: List[(String, InputFile)] = List("videoNote" -> videoNote)
  }

  /** Use this method to send audio files, if you want Telegram clients to display the file as a playable voice message.
   * For this to work, your audio must be in an .ogg file encoded with OPUS (other formats may be sent as Audio or Document).
   * On success, the sent Message is returned.
   * Bots can currently send voice messages of up to 50 MB in size, this limit may be changed in the future.
   *
   * @param chatId              Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param voice               InputFile or String Audio file to send.
   *                            Pass a file_id as String to send a file that exists on the Telegram servers (recommended),
   *                            pass an HTTP URL as a String for Telegram to get a file from the Internet, or upload a new one using multipart/form-data.
   * @param caption             String Optional Voice message caption, 0-200 characters
   * @param parseMode           String Optional Send Markdown or HTML, if you want Telegram apps to show bold, italic,
   *                            fixed-width text or inline URLs in the media caption.
   * @param duration            Integer Optional Duration of sent audio in seconds
   * @param disableNotification Boolean Optional Sends the message silently.
   *                            iOS users will not receive a notification, Android users will receive a notification with no sound.
   * @param replyToMessageId    Integer Optional If the message is a reply, ID of the original message
   * @param replyMarkup         InlineKeyboardMarkup or ReplyKeyboardMarkup or ReplyKeyboardHide or ForceReply Optional Additional interface options.
   *                            A JSON-serialized object for an inline keyboard, custom reply keyboard,
   *                            instructions to hide reply keyboard or to force a reply from the user.
   */
  case class SendVoice(chatId: ChatId,
                       voice: InputFile,
                       caption: Option[String] = None,
                       parseMode: Option[ParseMode] = None,
                       duration: Option[Int] = None,
                       disableNotification: Option[Boolean] = None,
                       replyToMessageId: Option[Int] = None,
                       replyMarkup: Option[ReplyMarkup] = None) extends MultipartApiRequest[Message] {
    override def getFiles: List[(String, InputFile)] = List("voice" -> voice)
  }

  /**
   * Use this method to change the description of a supergroup or a channel.
   * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
   * Returns True on success.
   *
   * @param chatId      Integer or String	Yes	Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param description String	No	New chat description, 0-255 characters pinChatMessage  *
   */
  case class SetChatDescription(chatId: ChatId, description: Option[String] = None) extends JsonApiRequest[Boolean]

  /** Use this method to set default chat permissions for all members.
   * The bot must be an administrator in the group or a supergroup for this to work and must have the can_restrict_members admin rights.
   * Returns True on success.
   *
   * @param chatId      Integer or String Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
   * @param permissions ChatPermissions New default chat permissions
   */
  case class SetChatPermissions(chatId: ChatId, permissions: ChatPermissions) extends JsonApiRequest[Boolean]

  /**
   * Use this method to set a new profile photo for the chat.
   * Photos can't be changed for private chats.
   * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
   * Returns True on success.
   *
   * '''Note:''' In regular groups (non-supergroups), this method will only work if the "All Members Are Admins" setting is off in the target group.
   *
   * @param chatId Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param photo  InputFile New chat photo, uploaded using multipart/form-data
   */
  case class SetChatPhoto(chatId: ChatId, photo: InputFile) extends MultipartApiRequest[Boolean] {
    override def getFiles: List[(String, InputFile)] = List("photo" -> photo)
  }

  /**
   * Use this method to set a new group sticker set for a supergroup.
   * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
   * Use the field can_set_sticker_set optionally returned in getChat requests to check if the bot can use this method. Returns True on success.
   *
   * @param chatId         Integer or String Yes	Unique identifier for the target chat or username of the target supergroup (in the format @supergroupusername)
   * @param stickerSetName String Yes Name of the sticker set to be set as the group sticker set
   */
  case class SetChatStickerSet(chatId: ChatId, stickerSetName: String) extends JsonApiRequest[Boolean]

  /**
   * Use this method to change the title of a chat.
   * Titles can't be changed for private chats.
   * The bot must be an administrator in the chat for this to work and must have the appropriate admin rights.
   * Returns True on success.
   *
   * '''Note:''' In regular groups (non-supergroups), this method will only work if the "All Members Are Admins" setting is off in the target group.
   *
   * @param chatId Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param title  String	New chat title, 1-255 characters
   */
  case class SetChatTitle(chatId: ChatId, title: String) extends JsonApiRequest[Boolean]

  /** Use this method to set the score of the specified user in a game.
   *
   * On success, if the message was sent by the bot, returns the edited Message,
   * otherwise returns True.
   * Returns an error, if the new score is not greater than the user's current
   * score in the chat and force is False.
   *
   * @param userId             Integer Yes User identifier
   * @param score              Integer Yes New score, must be positive
   * @param force              Boolean Optional Pass True, if the high score is allowed to decrease.
   *                           This can be useful when fixing mistakes or banning cheaters
   * @param disableEditMessage Boolean Optional Pass True, if the game message should not be automatically edited to include the current scoreboard
   * @param chatId             Integer or String Optional Required if inline_message_id is not specified.
   *                           Unique identifier for the target chat (or username of the target channel in the format @channelusername)
   * @param messageId          Integer Optional Required if inline_message_id is not specified. Unique identifier of the sent message
   * @param inlineMessageId    String Optional Required if chat_id and message_id are not specified. Identifier of the inline message
   */
  case class SetGameScore(userId: Int,
                          score: Long,
                          force: Option[Boolean] = None,
                          disableEditMessage: Option[Boolean] = None,
                          chatId: Option[ChatId] = None,
                          messageId: Option[Int] = None,
                          inlineMessageId: Option[String] = None) extends JsonApiRequest[Either[Boolean, Message]] {

    if (inlineMessageId.isEmpty) {
      require(chatId.isDefined, "Required if inlineMessageId is not specified")
      require(messageId.isDefined, "Required if inlineMessageId is not specified")
    }

    if (chatId.isEmpty && messageId.isEmpty)
      require(inlineMessageId.isDefined, "Required if chatId and messageId are not specified")
  }

  /**
   * Use this method to move a sticker in a set created by the bot to a specific position.
   * Returns True on success.
   *
   * @param sticker  String File identifier of the sticker
   * @param position Integer New sticker position in the set, zero-based
   */
  case class SetStickerPositionInSet(sticker: String, position: Int) extends JsonApiRequest[Boolean]

  /** Use this method to specify a url and receive incoming updates via an outgoing webhook.
   * Whenever there is an update for the bot, we will send an HTTPS POST request to the specified url, containing a JSON-serialized Update.
   * In case of an unsuccessful request, we will give up after a reasonable amount of attempts. Returns true.
   * If you'd like to make sure that the Webhook request comes from Telegram, we recommend using a secret path in the URL, e.g. https://www.example.com/<token>.
   * Since nobody else knows your bot‘s token, you can be pretty sure it’s us.
   *
   * @param url            String Yes HTTPS url to send updates to. Use an empty string to remove webhook integration
   * @param certificate    InputFile Optional Upload your public key certificate so that the root certificate in use can be checked. See our self-signed guide for details.
   * @param maxConnections Integer Optional Maximum allowed number of simultaneous HTTPS connections to the webhook for update delivery, 1-100. Defaults to 40. Use lower values to limit the load on your bot‘s server, and higher values to increase your bot’s throughput.
   * @param allowedUpdates Array of String Optional List the types of updates you want your bot to receive.
   *                       For example, specify [“message”, “edited_channel_post”, “callback_query”] to only receive updates of these types.
   *                       See Update for a complete list of available update types.
   *                       Specify an empty list to receive all updates regardless of type (default).
   *                       If not specified, the previous setting will be used.
   *
   *                       Please note that this parameter doesn't affect updates created before the call to the setWebhook, so unwanted updates may be received for a short period of time.
   *
   *                       '''Notes'''
   *   1. You will not be able to receive updates using getUpdates for as long as an outgoing webhook is set up.
   *   2. To use a self-signed certificate, you need to upload your public key certificate using certificate parameter.
   *   3. Ports currently supported for Webhooks: 443, 80, 88, 8443.
   *
   *                       NEW! If you're having any trouble setting up webhooks, please check out this [[https://core.telegram.org/bots/webhooks amazing guide to Webhooks]].
   */
  case class SetWebhook(url: String,
                        certificate: Option[InputFile] = None,
                        maxConnections: Option[Int] = None,
                        allowedUpdates: Option[Seq[UpdateType]] = None) extends MultipartApiRequest[Boolean] {
    override def getFiles: List[(String, InputFile)] = certificate.map("certificate" -> _).toList
  }

  /**
   * Use this method to stop updating a live location message sent by the bot or via the bot (for inline bots) before live_period expires.
   * On success, if the message was sent by the bot, the sent Message is returned, otherwise True is returned.
   *
   * @param chatId          Integer or String Optional Required if inline_message_id is not specified.
   *                        Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param messageId       Integer Optional Required if inline_message_id is not specified. Identifier of the sent message
   * @param inlineMessageId String Optional Required if chat_id and message_id are not specified. Identifier of the inline message
   * @param replyMarkup     InlineKeyboardMarkup Optional	A JSON-serialized object for a new inline keyboard.
   */
  case class StopMessageLiveLocation(chatId: Option[ChatId] = None,
                                     messageId: Option[Int] = None,
                                     inlineMessageId: Option[Int] = None,
                                     replyMarkup: Option[InlineKeyboardMarkup] = None) extends JsonApiRequest[Message Either Boolean]

  /**
   * Use this method to stop a poll which was sent by the bot.
   * On success, the stopped Poll with the final results is returned.
   *
   * @param chatId      Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   * @param messageId   Identifier of the original message with the poll
   * @param replyMarkup A JSON-serialized object for a new message inline keyboard.
   */
  case class StopPoll(chatId: ChatId,
                      messageId: Option[Int] = None,
                      replyMarkup: Option[ReplyMarkup] = None) extends JsonApiRequest[Poll]

  /** Use this method to unban a previously kicked user in a supergroup.
   * The user will not return to the group automatically, but will be able to join via link, etc.
   * The bot must be an administrator in the group for this to work. Returns True on success.
   *
   * @param chatId Integer or String Unique identifier for the target group or username of the target supergroup (in the format @supergroupusername)
   * @param userId Integer Unique identifier of the target user
   */
  case class UnbanChatMember(chatId: ChatId, userId: Int) extends JsonApiRequest[Boolean]

  /**
   * Use this method to unpin a message in a supergroup chat.
   * The bot must be an administrator in the chat for this to work and must have the ‘can_pin_messages’ admin right in
   * the supergroup or ‘can_edit_messages’ admin right in the channel.
   * Returns True on success.
   *
   * @param chatId Integer or String Unique identifier for the target chat or username of the target channel (in the format @channelusername)
   */
  case class UnpinChatMessage(chatId: ChatId) extends JsonApiRequest[Boolean]

  /**
   * Use this method to upload a .png file with a sticker for later use in createNewStickerSet
   * and addStickerToSet methods (can be used multiple times).
   * Returns the uploaded File on success.
   *
   * @param userId     Integer User identifier of sticker file owner
   * @param pngSticker InputFile Png image with the sticker, must be up to 512 kilobytes in size,
   *                   dimensions must not exceed 512px, and either width or height must be exactly 512px.
   *                   [[https://core.telegram.org/bots/api#sending-files More info on Sending Files]]
   */
  case class UploadStickerFile(userId: Int, pngSticker: InputFile) extends MultipartApiRequest[File] {
    override def getFiles: List[(String, InputFile)] = List("png_sticker" -> pngSticker)
  }

}
