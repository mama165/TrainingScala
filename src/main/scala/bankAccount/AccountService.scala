package bankAccount

import bankAccount.AccountError.{AmountMalformedError, AmountNegativeError}
import bankAccount.Types.Amount

import scala.util.Try

trait AccountServiceApi {
  def deposit(accountID: Long, strAmount: String): Unit

  def withdraw(accountId: Long, strAmount: String): Unit
}

class AccountService(operationRepository: OperationRepository) extends AccountServiceApi {

  def getAmount(strAmount: String): Either[Throwable, Amount] = {
    Try(strAmount.toLong).toEither
  }

  override def deposit(accountID: Long, strAmount: String): Unit = {
    validateAmount(accountID, strAmount) match {
      case Left(e) => e match {
        case AmountMalformedError(_) => println("The amount is malformed")
        case AmountNegativeError(negativeAmount) => println(s"The amount is negative : $negativeAmount")
      }
      case Right(amount) => operationRepository.findAll(accountID)
    }


  }

  def validateAmount(accountID: Long, strAmount: String): Either[AccountError, Amount] = {
    for {
      amount <- getAmount(strAmount).left.map(AmountMalformedError)
      _ <- Either.cond(amount > 0, (), AmountNegativeError(amount))
    } yield amount
  }

  override def withdraw(accountId: Long, strAmount: String) = ???
}
