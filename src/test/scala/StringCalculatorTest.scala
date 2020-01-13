import StringCalculator.StringCalculator
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.prop.TableDrivenPropertyChecks._

class StringCalculatorTest extends WordSpec with Matchers {
  val calculator = new StringCalculator

  "A calculator should" should {
    "sum two integers" in {
      calculator.add("1,2") shouldBe 3
    }
    "return 0 when empty string " in {
      calculator.add("") shouldBe 0
    }
    "sum unknown integers" in {
      calculator.add("1,2,3") shouldBe 6
    }
    "sum integers separated by new line or comma" in {
      calculator.add("1\n2") shouldBe 3

      val integersAsString = Table(
        ("input", "expected"),
        ("1\n2", 3),
        ("1\n2\n4", 7),
        ("1\n2,3", 6),
        ("1,\n", 1)
      )
      forAll(integersAsString) { (input: String, expected: Int) =>
        calculator.add(input) shouldBe expected
      }
    }
    "sum integers separated by a custom separator" in {
      val integersAsString = Table(
        ("input", "expected"),
        ("//;\n1;2", 3),
        ("//=\n6=8", 14),
        ("//@\n20@6@6", 32),
        ("//:\n20:6:6", 32)
      )

      forAll(integersAsString) { (input: String, expected: Int) =>
        calculator.add(input) shouldBe expected
      }
    }


    "throw an exception" in {
      val integersAsString = Table(
        ("input", "expected"),
        ("1,-2", "Negative number are not allowed : -2"),
        ("-30,-62,-34,4,-6", "Negative number are not allowed : -30, -62, -34, -6"),
        ("-71,-62,3,-9,89", "Negative number are not allowed : -71, -62, -9"),
        ("//:\n-20:-6:6", "Negative number are not allowed : -20, -6")
      )

      forAll(integersAsString) { (input: String, messageExpected: String) =>
        the[RuntimeException] thrownBy {
          calculator.add(input)
        } should have message (messageExpected)
      }
    }
  }
}
