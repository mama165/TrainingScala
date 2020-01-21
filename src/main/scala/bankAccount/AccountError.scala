package bankAccount

sealed trait AccountError

object AccountError {
  case class AmountNegativeError(strAmount: String) extends AccountError
  case class AccountNotFoundError(accountID: Long) extends AccountError
  case class AmountMalformedError(strAmount: String) extends AccountError
  case class NotEnoughMoneyError(strAmount: String) extends AccountError
}
