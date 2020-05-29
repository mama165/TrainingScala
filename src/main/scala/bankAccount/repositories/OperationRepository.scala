package bankAccount.repositories

import bankAccount.models.Operation

trait OperationRepository {
  def findAll(accountId: Long): List[Operation]
  def add(operation: Operation): Unit
}
