package bankAccount

import java.time._

import bankAccount.errors.AccountError.{
  AmountMalformedError,
  AmountNegativeError
}
import bankAccount.models.{OperationDeposit, OperationWithdrawal}
import bankAccount.printers.OperationPrinter
import bankAccount.repositories.OperationRepository
import bankAccount.services.AccountService
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest._
import org.scalatestplus.mockito.MockitoSugar.mock

class AccountServiceSpec
    extends WordSpec
    with Matchers
    with EitherValues
    with GivenWhenThen {
  private implicit val fixedClock: Clock = Constants.CLOCK
  private val operationPrinter: OperationPrinter = mock[OperationPrinter]

  private def loanPattern(
      exposeToTest: (AccountService,
                     OperationRepository,
                     OperationPrinter) => Unit): Unit = {
    val operationRepository: OperationRepository = mock[OperationRepository]
    val accountService: AccountService = new AccountService(operationRepository)
    val operationPrinter = mock[OperationPrinter]
    exposeToTest(accountService, operationRepository, operationPrinter)
  }

  "When deposit an amount should" should {

    "throw when the amount is malformed" in loanPattern {
      (accountService, operationRepository, _) =>
        val result = accountService.validateAmount("john.wick")

        result.left.value shouldBe a[AmountMalformedError]
        verify(operationRepository, never()).add(any())
    }

    "throw when the amount is negative" in loanPattern {
      (accountService, operationRepository, _) =>
        val result = accountService.validateAmount("-1")

        result.left.value shouldBe a[AmountNegativeError]
        verify(operationRepository, never()).add(any())
    }

    "record an operation when deposit" in loanPattern {
      (accountService, operationRepository, _) =>
        accountService.deposit(1L, "10")

        verify(operationRepository, times(1))
          .add(OperationDeposit(1L, 10L, fixedClock))
    }
  }

  "When withdrawal an amount should" should {
    "throw when not enough money (withdrawal )" in loanPattern {
      (accountService, operationRepository, _) =>
        val operationList = List(OperationDeposit(1L, 20L, fixedClock),
                                 OperationWithdrawal(1L, 10L, fixedClock))
        when(operationRepository.findAll(1L)).thenReturn(operationList)

        accountService.withdrawal(1L, "20")

        verify(operationRepository, never()).add(any())
    }

    "record an operation when withdrawal" in loanPattern {
      (accountService, operationRepository, _) =>
        val operationList = List(OperationDeposit(1L, 20L, fixedClock),
                                 OperationWithdrawal(1L, 10L, fixedClock))
        when(operationRepository.findAll(1L)).thenReturn(operationList)

        accountService.withdrawal(1L, "5")

        verify(operationRepository, times(1))
          .add(OperationWithdrawal(1L, 5L, fixedClock))
    }
  }

  "When asking history should" should {
    "print statements" in loanPattern({
      (accountService, operationRepository, operationPrinter) =>
        val operations = List(OperationDeposit(1L, 20L, fixedClock),
                              OperationWithdrawal(1L, 10L, fixedClock),
                              OperationDeposit(1L, 20L, fixedClock))

        when(operationRepository.findAll(any())).thenReturn(operations)

        accountService.printStatements(operationPrinter, 1L)

        verify(operationPrinter, times(1)).print(operations, 30L)
    })
  }
}
