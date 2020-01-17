package bankAccount

import java.time.Instant

import bankAccount.Types.Amount

sealed trait Operation

case class OperationDeposit(accountID: Long, amount: Amount, instant: Instant) extends Operation
case class OperationWithdrawal(accountID: Long, amount: Amount, instant: Instant) extends Operation