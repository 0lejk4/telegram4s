# Telegram4s

## Description

Functional Telegram Bot Framework for Scala based on http4s.
Written in a tagless final, so you can plug your preferred concurrency abstraction.

## Example

```scala
val program = 
    Telegram4sClient[IO](token) // Bot Api Client
      .use { client =>
        Telegram4s(client) // Bot Builder
          .onUpdate(println) // Add update handler
          .poll() // Starts polling
      }
      .either
      .map(_.fold(_ => 1, _ => 0))
```

## Installation

In progress

##Modules

* *Core* - all you need to create a telegram bot
* *Examples* - example bot built using *Core* module and ZIO