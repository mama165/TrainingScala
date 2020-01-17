package bankAccount

trait OperationRepository {
  def findAll(accountId: Long): List[Operation] // devrait return List[Operations]
  def add(operation: Operation): Unit
}
