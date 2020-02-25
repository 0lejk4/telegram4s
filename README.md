# Telegram4s
[![Build Status](https://travis-ci.org/0lejk4/telegram4s.svg?branch=master)](https://travis-ci.org/0lejk4/telegram4s)
[![Latest version](https://index.scala-lang.org/0lejk4/telegram4s/telegram4s-core/latest.svg?color=orange&v=1)](https://index.scala-lang.org/0lejk4/telegram4s/telegram4s-core)
## Description

Functional Telegram Bot Framework for Scala based on http4s.
Written in a tagless final, so you can plug your preferred concurrency abstraction.

## Documenation
You can start using telegram4s with help of Telegram4s [microsite](https://0lejk4.github.io/telegram4s/)

## Example

```scala
Telegram4sClient[Task](token)
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
```

## Installation

In progress

##Modules

* *Core* - all you need to create a telegram bot
* *Examples* - example bot built using *Core* module and ZIO