package telegram4s.models

/**
  * Represents Telegram voice note
  */
final case class Voice(fileId: String, duration: Int, mimeType: Option[String], fileSize: Option[Int])
