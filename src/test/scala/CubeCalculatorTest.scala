import CubeCalculator.CubeCalculator
import org.scalatest.WordSpec

class CubeCalculatorTest extends WordSpec {
  val cubeCalculator = new CubeCalculator

  "A test" should {
    "blabla" in {
      assert(cubeCalculator.cube(3) == 27)
    }
  }
}
