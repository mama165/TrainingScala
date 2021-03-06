package stringCalculator

import scala.util.Try

class StringCalculator {
  val COMMA_SEPARATOR = ','
  val DELIMITER = ",|\n"
  val EMPTY_VALUE = ""

  def add(integers: String): Int = {
    val pattern = raw"//.+\n".r

    pattern.findFirstIn(integers) match {
      case Some(_) =>
        val inputExtracted = integers.substring(4)
        val specificDelimiter = integers.slice(2, 3)
        sumIntegersWithDelimiter(inputExtracted, specificDelimiter)
      case None => sumIntegersWithDelimiter(integers, DELIMITER)
    }
  }

  private def sumIntegersWithDelimiter(input: String, delimiter: String): Int = {
    val list = input.split(delimiter)
      .flatMap(strInt => Try(strInt.toInt).toOption)
      .toList
    val (negativeInts, positiveInts) = list.partition(_ < 0)
    if (negativeInts.nonEmpty) throw new RuntimeException(s"Negative number are not allowed : ${negativeInts.mkString(", ")}")
    else positiveInts.filter(_ < 1000).sum
  }
}
