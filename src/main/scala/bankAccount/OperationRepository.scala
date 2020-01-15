package bankAccount

trait OperationRepository {
  def findAll(accountId: Long)
}
