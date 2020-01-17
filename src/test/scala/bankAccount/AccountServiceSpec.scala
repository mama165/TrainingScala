package bankAccount

import java.time.{Clock, Instant, ZoneId}

import bankAccount.AccountError.{AmountMalformedError, AmountNegativeError}
import org.scalatest.{EitherValues, GivenWhenThen, Matchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar.mock
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.mockito.BDDMockito.given

class AccountServiceSpec extends WordSpec with Matchers with EitherValues  with GivenWhenThen{
  val mockOperationRepository: OperationRepository = mock[OperationRepository]
  val fixedDate = Instant.now

  "When deposit an amount should" should {

    "throw when the amount is malformed" in {
      val service = new AccountService(mockOperationRepository, mock[Clock])
      val result = service.validateAmount(1L, "john.wick")

      result.left.value shouldBe a[AmountMalformedError]
      verify(mockOperationRepository, never()).add(any())
    }

    "throw when the amount is negative" in {
      val service = new AccountService(mockOperationRepository, mock[Clock])
      val result = service.validateAmount(1L, "-1")

      result.left.value shouldBe a[AmountNegativeError]
      verify(mockOperationRepository, never()).add(any())
    }

    "throw when not enough money (withdrawal )" in {
      val service = new AccountService(mockOperationRepository, Clock.fixed(fixedDate, ZoneId.of("Europe/Paris")))
      service.withdraw(1L, "10")

      verify(mockOperationRepository, never()).add(any())
    }

    "record an operation when deposit" in {
      val service = new AccountService(mockOperationRepository, Clock.fixed(fixedDate, ZoneId.of("Europe/Paris")))
      service.deposit(1L, "10")

      verify(mockOperationRepository, times(1)).add(Operation(1L, 10L, fixedDate))
    }
  }
}
