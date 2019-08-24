package telegram4s

import org.http4s.dsl.Http4sDsl
import telegram4s.Common.Effect
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.Console
import zio.interop.catz._
import zio.random.Random
import zio.system.System
import zio.{TaskR, ZIO}

import scala.concurrent.ExecutionContext.Implicits.global

object Common {
  type Environment = Clock with Console with System with Random with Blocking
  type Effect[T] = TaskR[Environment, T]
}

object EffectDsl extends Http4sDsl[Effect]

object Examples extends CatsApp {

  val token: String = sys.env.getOrElse("BOT_TOKEN", sys.exit())

  override def run(args: List[String]): ZIO[Environment, Nothing, Int] = {
    Telegram4sClient[Effect](token)
      .use { client =>
        Telegram4s(client)
          .onUpdate(println)
          .poll()
      }
      .either
      .map(_.fold(_ => 1, _ => 0))
  }
}
