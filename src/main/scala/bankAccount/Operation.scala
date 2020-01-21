package bankAccount

import java.time.Clock

import bankAccount.Types.Amount

sealed trait Operation

case class OperationDeposit(accountID: Long, amount: Amount, clock: Clock) extends Operation

case class OperationWithdrawal(accountID: Long, amount: Amount, clock: Clock) extends Operation
