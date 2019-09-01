package telegram4s.models.messages

import telegram4s.models.{Chat, User, VideoNote}

final case class VideoNoteMessage(messageId: Int,
                                  chat: Chat,
                                  date: Int,
                                  videoNote: VideoNote,
                                  from: Option[User] = None,
                                  forwardFrom: Option[User] = None,
                                  forwardFromChat: Option[Chat] = None,
                                  forwardFromMessageId: Option[Int] = None,
                                  forwardSignature: Option[String] = None,
                                  forwardSenderName: Option[String] = None,
                                  forwardDate: Option[Int] = None,
                                  replyToMessage: Option[TelegramMessage] = None,
                                  editDate: Option[Int] = None,
                                  authorSignature: Option[String] = None)
    extends UserMessage
