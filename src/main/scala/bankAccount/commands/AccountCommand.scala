package bankAccount.commands

import bankAccount.Constants
import bankAccount.features.DiscountCalculator
import bankAccount.models.OperationDeposit
import bankAccount.models.Types.Amount
import bankAccount.repositories._

sealed trait AccountCommand {}

sealed trait DepositCommand extends AccountCommand

case class DepositStandardCommand(operationRepository: OperationRepository)
    extends DepositCommand {
  def execute(depositStandardRequest: DepositStandardRequest,
              depositStandardPresenter: DepositStandardPresenter): Unit = {
    operationRepository.add(
      OperationDeposit(depositStandardRequest.accountID,
                       depositStandardRequest.amount,
                       Constants.CLOCK))

    depositStandardPresenter.copy(
      response = Some(DepositStandardResponse(println("I'm the response"))))
  }
}

case class DepositWhenLoyalCommand(operationRepository: OperationRepository)
    extends DepositCommand {
  def execute(request: DepositWhenLoyalRequest,
              presenter: DepositWhenLoyalPresenter): Unit = {
    val discount: Amount =
      DiscountCalculator.applyDiscount(request.years)
    val unit = operationRepository.add(
      OperationDeposit(request.accountID, discount, Constants.CLOCK))

    presenter.copy(response = Some(DepositWhenLoyalResponse(discount, "Errrrror !!")))
  }
}

case class DepositStandardRequest(accountID: Long, amount: Amount)
    extends OperationRequest

case class DepositWhenLoyalRequest(accountID: Long, years: Int)
    extends OperationRequest

case class WithdrawalRequest(accountID: Long, amount: Amount)
    extends OperationRequest

sealed case class WithdrawalCommand(operationRepository: OperationRepository)
    extends AccountCommand {
  override def execute(operationRequest: OperationRequest,
                       operationPresenter: OperationPresenter): Unit = ???
}

sealed case class PrintCommand() extends AccountCommand {
  override def execute(): PrintResponse =
    PrintResponse(println("I'm the commander"))
}
sealed case class ViewCommand(presentRequest: PresenterRequest)
    extends AccountCommand {
  override def execute(): ViewResponse =
    ViewResponse(println("I'm the viewer"))
}

sealed trait PresenterRequest
case class OperationView(operationRepository: OperationRepository,
                         accountID: Long)
    extends PresenterRequest
