package bankAccount

sealed trait OperationFormatter {
  def format(operations: List[Operation], balance: Long): String
}

class TextOperationFormatter extends OperationFormatter {
  override def format(operations: List[Operation], balance: Long): String = ???
}

