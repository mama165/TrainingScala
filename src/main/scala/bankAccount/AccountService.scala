package bankAccount

import java.time.Clock

import bankAccount.AccountError.{AmountMalformedError, AmountNegativeError, NotEnoughMoneyError}
import bankAccount.Types.Amount

import scala.util.Try

trait AccountServiceApi {
  def deposit(accountID: Long, strAmount: String): Unit

  def withdrawal(accountId: Long, strAmount: String): Unit
}

class AccountService(operationRepository: OperationRepository)(implicit clock: Clock) extends AccountServiceApi {

  def getAmount(strAmount: String): Either[Throwable, Amount] = {
    Try(strAmount.toLong).toEither
  }

  def validateAmount(accountID: Long, strAmount: String): Either[AccountError, Amount] = {
    for {
      amount <- getAmount(strAmount).left.map(_ => AmountMalformedError(strAmount))
      _ <- Either.cond(amount > 0, (), AmountNegativeError(strAmount))
    } yield amount
  }

  override def deposit(accountID: Long, strAmount: String): Unit = {
    validateAmount(accountID, strAmount) match {
      case Left(e) => e match {
        case AmountMalformedError(_) => println("The amount is malformed")
        case AmountNegativeError(negativeAmount) => println(s"The amount is negative : $negativeAmount")
      }
      case Right(amount) => operationRepository.add(OperationDeposit(accountID, amount, clock))
    }
  }

  override def withdrawal(accountID: Long, strAmount: String): Unit = {
    val resultOfOperations = for {
      amount <- validateAmount(accountID, strAmount)
      _ <- Either.cond(computeBalance(accountID) >= amount, (), NotEnoughMoneyError(strAmount))
    } yield amount

    resultOfOperations match {
      case Left(AmountMalformedError(amount)) => println("The amount is malformed : " + amount)
      case Left(AmountNegativeError(amount)) => println("The amount is negative : " + amount)
      case Left(NotEnoughMoneyError(amount)) => println("Withdrawal impossible : " + amount)
      case Right(amount) => operationRepository.add(OperationWithdrawal(accountID, amount, clock))
    }
  }

  def computeBalance(accountID: Long) = {
    val operations = operationRepository.findAll(accountID)
    operations.foldLeft(0L) {
      (balance, operation) =>
        operation match {
          case OperationDeposit(_, amount, _) => balance + amount
          case OperationWithdrawal(_, amount, _) => balance - amount
        }
    }
  }

}