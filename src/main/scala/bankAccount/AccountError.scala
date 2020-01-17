package bankAccount

import bankAccount.Types.Amount

sealed trait AccountError

object AccountError {
  case class AmountNegativeError(amount: Amount) extends AccountError
  case class AccountNotFoundError(accountID: Long) extends AccountError
  case class AmountMalformedError(error: Throwable) extends AccountError
  case class NotEnoughMoneyError(amount: Amount) extends AccountError
}
