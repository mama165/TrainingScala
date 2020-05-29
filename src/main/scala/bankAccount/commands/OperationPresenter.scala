package bankAccount.commands

import bankAccount.repositories._

trait OperationPresenter {}

case class DepositStandardPresenter(response: Option[DepositResponse] = None) extends OperationPresenter
case class DepositWhenLoyalPresenter(response: Option[DepositWhenLoyalResponse] = None) extends OperationPresenter
case class WithdrawalPresenter(response: Option[WithdrawalResponse] = None) extends OperationPresenter
