package bankAccount

import scala.util.Try

object Types {
  type Amount = Long

  def getAmount(strAmount: Amount): Either[Throwable, Amount] = {
    Try(strAmount.toLong).toEither
  }
}