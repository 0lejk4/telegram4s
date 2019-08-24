package telegram4s

import io.circe._

package object marshalling extends CirceEncoders with CirceDecoders with StringUtils {
  val printer: Printer = Printer.noSpaces.copy(dropNullValues = true)
}
