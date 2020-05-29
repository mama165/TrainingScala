package bankAccount.formatters

import bankAccount.models.Operation

sealed trait OperationFormatter {
  def format(operations: List[Operation], balance: Long): String
}

class TextOperationFormatter extends OperationFormatter {
  override def format(operations: List[Operation], balance: Long): String = ???
}
