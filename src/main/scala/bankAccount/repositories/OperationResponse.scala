package bankAccount.repositories

sealed trait OperationResponse

trait DepositResponse extends OperationResponse

case class DepositStandardResponse(response: Unit) extends DepositResponse
case class DepositWhenLoyalResponse(discount: Long, error: String) extends DepositResponse


case class WithdrawalResponse(response: Unit) extends OperationResponse
case class PrintResponse(response: Unit) extends OperationResponse
case class ViewResponse(response: Unit) extends OperationResponse
