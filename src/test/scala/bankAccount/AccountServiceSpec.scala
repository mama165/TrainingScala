package bankAccount

import java.time.{Clock, Instant, ZoneId}

import bankAccount.AccountError.{AmountMalformedError, AmountNegativeError}
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.{EitherValues, GivenWhenThen, Matchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar.mock

class AccountServiceSpec extends WordSpec with Matchers with EitherValues  with GivenWhenThen{
 implicit  val fixedClock  = Clock.fixed( Instant.now, ZoneId.of("Europe/Paris"))
  val mockOperationRepository: OperationRepository = mock[OperationRepository]

  "When deposit an amount should" should {

    "throw when the amount is malformed" in {
      val service = new AccountService(mockOperationRepository)
      val result = service.validateAmount(1L, "john.wick")

      result.left.value shouldBe a[AmountMalformedError]
      verify(mockOperationRepository, never()).add(any())
    }

    "throw when the amount is negative" in {
      val service = new AccountService(mockOperationRepository)
      val result = service.validateAmount(1L, "-1")

      result.left.value shouldBe a[AmountNegativeError]
      verify(mockOperationRepository, never()).add(any())
    }

    "throw when not enough money (withdrawal )" in {
      val service = new AccountService(mockOperationRepository)
      val operationList = List(OperationDeposit(1L, 10L, fixedClock), OperationWithdrawal(1L, 5L, fixedClock))
      when(mockOperationRepository.findAll(1L)).thenReturn(operationList)

      service.withdrawal(1L, "10")

      verify(mockOperationRepository, never()).add(any())
    }

    "record an operation when withdrawal" in {
      val service = new AccountService(mockOperationRepository)
      val operationList = List(OperationDeposit(1L, 20L, fixedClock), OperationWithdrawal(1L, 10L, fixedClock))
      when(mockOperationRepository.findAll(1L)).thenReturn(operationList)

      service.withdrawal(1L, "5")

      verify(mockOperationRepository, times(1)).add(OperationWithdrawal(1L, 5L, fixedClock))
    }

    "record an operation when deposit" in {
      val service = new AccountService(mockOperationRepository)
      service.deposit(1L, "10")

      verify(mockOperationRepository, times(1)).add(OperationDeposit(1L, 10L, fixedClock))
    }
  }
}
