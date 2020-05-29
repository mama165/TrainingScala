package bankAccount.repositories

import bankAccount.models.Operation

trait OperationRequest {
  def saveOperation(operationRepository: OperationRepository,
                    operation: Operation): Unit =
    operationRepository.add(operation)
}
