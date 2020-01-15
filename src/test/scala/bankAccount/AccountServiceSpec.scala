package bankAccount

import bankAccount.AccountError.{AmountMalformedError, AmountNegativeError}
import org.scalatest.{EitherValues, Matchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar.mock

class AccountServiceSpec extends WordSpec with Matchers with EitherValues {
  val mockOperationRepository: OperationRepository = mock[OperationRepository]

  "When deposit an amount should" should {

    "throw when the amount is malformed" in {
      val service = new AccountService(mockOperationRepository)
      val result = service.validateAmount(1L, "john.wick")

      result.left.value shouldBe a[AmountMalformedError]
    }

    "throw when the amount is negative" in {
      val service = new AccountService(mockOperationRepository)
      val result = service.validateAmount(1L, "-1")

      result.left.value shouldBe a[AmountNegativeError]
    }
  }
}
