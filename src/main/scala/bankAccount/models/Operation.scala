package bankAccount.models

import java.time.Clock

import Types.Amount

sealed trait Operation

case class OperationDeposit(accountID: Long, amount: Amount, clock: Clock) extends Operation
case class OperationWithdrawal(accountID: Long, amount: Amount, clock: Clock) extends Operation
case class OperationDiscount(accountID: Long, discount: Amount, clock: Clock) extends Operation
