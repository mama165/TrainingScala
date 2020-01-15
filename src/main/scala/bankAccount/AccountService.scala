package bankAccount

import bankAccount.AccountError.{AmountNegativeError, AmountMalformedError}
import bankAccount.Types.Amount

import scala.util.Try

trait AccountServiceApi {
  def deposit(accountID: Long, strAmount: String):  Either[AccountError, Amount]

  def withdraw(accountId: Long, strAmount: String): Either[AccountError, Amount]
}

trait AccountService extends AccountServiceApi {

  def getAmount(strAmount: String): Either[Throwable, Amount] = {
    Try(strAmount.toLong).toEither
  }

  override def deposit(accountID: Long, strAmount: String):  Either[AccountError, Amount]  = {
    for {
      amount <- getAmount(strAmount).left.map(AmountMalformedError)
      _ <- Either.cond(amount > 0, (), AmountNegativeError(amount))
    } yield amount
  }

  override def withdraw(accountId: Long, strAmount: String) = ???
}
