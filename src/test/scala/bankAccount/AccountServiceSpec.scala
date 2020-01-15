package bankAccount

import bankAccount.AccountError.{AmountMalformedError, AmountNegativeError}
import org.scalatest.{EitherValues, Matchers, WordSpec}


class AccountServiceSpec extends WordSpec with Matchers with EitherValues {
  "blabla" should {
    "hello" in {
      val service = new AccountService {}
      val result = service.deposit(1L, "-1")

      result.left.value shouldBe a[AmountNegativeError]
    }

    "hellohhg" in {
      val service = new AccountService {}
      val result = service.deposit(1L, "john.wick")

      result.left.value shouldBe a[AmountMalformedError]
    }
  }
}
