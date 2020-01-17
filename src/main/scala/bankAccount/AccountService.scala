package bankAccount

import java.time.{Clock, Instant}

import bankAccount.AccountError.{AmountMalformedError, AmountNegativeError, NotEnoughMoneyError}
import bankAccount.Types.Amount

import scala.util.Try

trait AccountServiceApi {
  def deposit(accountID: Long, strAmount: String): Unit

  def withdraw(accountId: Long, strAmount: String): Unit
}

class AccountService(operationRepository: OperationRepository, clock: Clock) extends AccountServiceApi {

  def getAmount(strAmount: String): Either[Throwable, Amount] = {
    Try(strAmount.toLong).toEither
  }

  def validateAmount(accountID: Long, strAmount: String): Either[AccountError, Amount] = {
    for {
      amount <- getAmount(strAmount).left.map(AmountMalformedError)
      _ <- Either.cond(amount > 0, (), AmountNegativeError(amount))
    } yield amount
  }

  override def deposit(accountID: Long, strAmount: String): Unit = {
    validateAmount(accountID, strAmount) match {
      case Left(e) => e match {
        case AmountMalformedError(_) => println("The amount is malformed")
        case AmountNegativeError(negativeAmount) => println(s"The amount is negative : $negativeAmount")
      }
      case Right(amount) => operationRepository.add(OperationWithdrawal(accountID, amount, Instant.now(clock)))
    }
  }

  //  def checkForBalance(accountID: Long): Either[NotEnoughMoneyError, Long] = {
  //
  //  }


  override def withdraw(accountID: Long, strAmount: String) = {
    //    validateAmount(accountID, strAmount) match {
    //      case Left(e) => e match {
    //        case AmountMalformedError(_) => println("The amount is malformed")
    //        case AmountNegativeError(negativeAmount) => println(s"The amount is negative : $negativeAmount")
    //      }
    //      //      case Right(amount) => operationRepository.add(Operation(accountID, amount, Instant.now(clock)))
    //      case Right(amount) => for {
    //        operation <- Either.cond(computeBalance(accountID) > amount, (), NotEnoughMoneyError(amount))
    //      } yield operation match {
    //        case Left(operation) => e
    //        case Right(_) => operationRepository.add(OperationDeposit(accountID, amount, Instant.now(Clock)))
    //      }
    //    }

    val result = for {
      amount <- validateAmount(accountID, strAmount)
      _ <- Either.cond(computeBalance(accountID) > amount, (), NotEnoughMoneyError(amount))
    } yield operationRepository.add(OperationWithdrawal(accountID, amount, Instant.now(clock)))

    result match {
      case Left(AmountMalformedError(e)) => ???
      case Left(AmountNegativeError(e)) => ???
      case Left(NotEnoughMoneyError(e)) => ???
      case Right(_) => ???
    }


  }


  val nums = List(1, 2, 3, 4)
  val sum = nums.foldLeft(0) {
    (acc, num) => acc + num
  }


  def computeBalance(accountID: Long) = {
    val operations = operationRepository.findAll(accountID)
    //    operations.foldLeft(0, {
    //      ( o1, o2) =>  )
    //    }
    0L
  }

}