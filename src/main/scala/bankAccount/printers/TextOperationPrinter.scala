package bankAccount.printers

import bankAccount.formatters.OperationFormatter
import bankAccount.models.Operation

sealed trait OperationPrinter {
  def print(operations: List[Operation], balance: Long): Unit
}

class TextOperationPrinter(operationFormatter: OperationFormatter)
    extends OperationPrinter {
  override def print(operations: List[Operation], balance: Long): Unit = {
    val formattedOperations = operationFormatter.format(operations, balance)
    println(formattedOperations)
  }
}
