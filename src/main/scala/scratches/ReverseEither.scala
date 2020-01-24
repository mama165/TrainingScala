package scratches

object ReverseEither extends App {

  val inputRightList: List[Either[String, Int]] = List(Right(1), Right(2), Right(3), Right(4), Right(5))
  val inputLeftList: List[Either[String, Int]] = List(Right(1), Left("NaN1"), Right(3), Left("NaN"), Right(5))
  inputRightList
  inputLeftList
  reverseGenericEither[String, Int](inputRightList)
  reverseGenericEither[String, Int](inputLeftList)

  //list2.foldLeft[Either[String, List[Int]]](Right(List())) {
  //  (accEither, element) => accEither.flatMap(list => element.map(list :+ _))
  //}

  def reverseEither(inputList: List[Either[String, Int]]) = {
    inputList.foldLeft[Either[String, List[Int]]](Right(List())) {
      (accEither, element) =>
        accEither match {
          case Left(value) => Left(value)
          case Right(list) => element match {
            case Left(value) => Left(value)
            case Right(value) => Right(list :+ value)
          }
        }
    }
  }

  def reverseGenericEither[T, U](inputList: List[Either[T, U]]) = {
    inputList.foldLeft[Either[T, List[U]]](Right(List())) {
      (accEither, element) =>
        accEither match {
          case Left(value) => Left(value)
          case Right(list) => element match {
            case Left(value) => Left(value)
            case Right(value) => Right(list :+ value)
          }
        }
    }
  }
}
