package bankAccount.services

import java.time.Clock

import bankAccount.UsesCases.Deposit.{DepositStandard, DepositWhenLoyal}
import bankAccount.UsesCases.Withdrawal.WithdrawStandard
import bankAccount.commands._
import bankAccount.errors.AccountError
import bankAccount.errors.AccountError._
import bankAccount.models.Types.Amount
import bankAccount.models._
import bankAccount.printers.OperationPrinter
import bankAccount.repositories.{
  DepositResponse,
  DepositWhenLoyalResponse,
  OperationRepository,
  PrintResponse,
  WithdrawalResponse
}

import scala.util.Try

trait AccountServiceApi {
  def deposit(accountID: Long, strAmount: String): Either[AccountError, Unit]

  def depositDiscount(accountID: Long, years: Int): Either[AccountError, Long]

  def withdrawal(accountId: Long, strAmount: String): Either[AccountError, Unit]

  def printStatements(operationPrinter: OperationPrinter,
                      accountID: Long): PrintResponse
}

class AccountService(operationRepository: OperationRepository)(
    implicit clock: Clock)
    extends AccountServiceApi {

  private def getAmount(strAmount: String): Either[AccountError, Amount] = {
    Try(strAmount.toLong).toEither.left.map(_ =>
      AmountMalformedError(strAmount))
  }

  def validateAmount(strAmount: String): Either[AccountError, Amount] =
    getAmount(strAmount)
      .flatMap(amount =>
        Either.cond(amount > 0, amount, AmountNegativeError(strAmount)))

  override def deposit(accountID: Long,
                       strAmount: String): Either[AccountError, Unit] = {
    validateAmount(strAmount).map(
      amount =>
        DepositStandardCommand(operationRepository)
          .execute(DepositStandardRequest(accountID, amount),
                   DepositStandardPresenter()))

  }
  override def depositDiscount(accountID: Long,
                               years: Int): Either[AccountError, Long] = {
    val presenter = DepositWhenLoyalPresenter()
    val request = DepositWhenLoyalRequest(accountID, years)
    val depositWhenLoyalCommand = DepositWhenLoyalCommand(operationRepository)

    depositWhenLoyalCommand
      .execute(request, presenter)

    presenter.response match {
      case Some(DepositWhenLoyalResponse(discount, error)) =>
        Right(response.discount)
      case _ => Left(???)
    }
  }

  override def withdrawal(
      accountID: Long,
      strAmount: String): Either[AccountError, WithdrawalResponse] = {

    for {
      amount <- validateAmount(strAmount)
      operations = operationRepository.findAll(accountID)
      _ <- Either.cond(computeBalance(operations) >= amount,
                       (),
                       NotEnoughMoneyError(strAmount))
    } yield {
      WithdrawalCommand(operationRepository,
                        WithdrawStandard(accountID, amount)).execute()
    }

  }

  override def printStatements(operationPrinter: OperationPrinter,
                               accountID: Long): PrintResponse = {
    val operationsList = operationRepository.findAll(accountID)

    //TODO: Recupérer une liste via la commande
    val operations: Unit = ViewCommand(
      OperationView(operationRepository, accountID))

    PrintResponse(
      operationPrinter.print(operationsList, computeBalance(operationsList)))
  }

  def computeBalance(operations: List[Operation]): Long = {
    operations.foldLeft(0L) { (balance, operation) =>
      operation match {
        case OperationDeposit(_, amount, _)    => balance + amount
        case OperationWithdrawal(_, amount, _) => balance - amount
        case OperationDiscount(_, discount, _) => balance + discount
      }
    }
  }
}
