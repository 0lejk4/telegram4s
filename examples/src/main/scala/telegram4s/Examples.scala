package telegram4s

import zio.console._
import zio.interop.catz._
import zio.{RIO, ZIO}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.reflectiveCalls

object Examples extends CatsApp {
  type Effect[T] = RIO[zio.ZEnv, T]

  val token: String = sys.env.getOrElse("BOT_TOKEN", sys.exit())

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, Int] = {
    Telegram4sClient[Effect](token)
      .use { implicit client =>
        Telegram4sBot.poll()
          .evalTap { update =>
            putStrLn(update.toString)
          }
          .compile
          .drain
      }
      .either
      .map(_.fold(_ => 1, _ => 0))
  }
}
