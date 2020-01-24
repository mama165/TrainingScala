package decisionTree

import cats.implicits._
import cats.syntax.bifunctor._
import cats.syntax.either._

import scala.io.Source

object DecisionTree extends App {

  sealed abstract class BaseLine(val data: String)

  case class ParentLine(override val data: String, strCond: String, yes: String, no: String) extends BaseLine(data)

  case class LeafLine(override val data: String, value: String) extends BaseLine(data)

  sealed abstract class Node(val value: String)

  case class ParentNode(override val value: String, nodeYes: Node, nodeNo: Node) extends Node(value)

  case class LeafNode(override val value: String) extends Node(value)

  sealed trait DecisionTreeError

  case class MalformedError(line: String) extends DecisionTreeError

  case class FileNotFound(fileName: String) extends DecisionTreeError

  case class PatternNotFound(line: String) extends DecisionTreeError


  //  val lines = List(
  //    "0:[device_type=pc||or||browser=7] yes=2,no=1",
  //    "2:[os_family=5] yes=6,no=5",
  //    "6:[browser=8] yes=12,no=11",
  //    "12:[language=2] yes=20,no=19",
  //    "20:leaf=0.000559453",
  //    "19:leaf=0.000594593",
  //    "11:[size=300x600] yes=18,no=17",
  //    "18:leaf=0.000597397",
  //    "17:leaf=0.00063461",
  //    "5:[browser=8||or||browser=5] yes=10,no=9",
  //    "10:leaf=0.000625534",
  //    "9:[position=2] yes=16,no=15",
  //    "16:leaf=0.00066727",
  //    "15:leaf=0.000708484",
  //    "1:[browser=8] yes=4,no=3",
  //    "4:leaf=0.000881108",
  //    "3:[os_family=5] yes=8,no=7",
  //    "8:leaf=0.000842268",
  //    "7:[region=FR:A5] yes=14,no=13",
  //    "14:leaf=0.000939982",
  //    "13:leaf=0.000999001",
  //  )

  //  val lines = Source.fromResource("decisionTree/tree_1.txt").getLines().toList
  //  val baseLines: Either[DecisionTreeError, List[BaseLine]] = buildBaseLines(lines)


  flattenDecisionTree("decisionTree/tree_1.txt") match {
    case Left(value) =>
    case Right(value) =>
  }

  private def flattenDecisionTree(fileName: String) = {
    for {
      list <- extractLinesFromFile(fileName)
      baseLines <- buildBaseLines(list)
      node <- buildRecursively(mapFromBaseLine(baseLines), baseLines.head)
    } yield node
  }

  private def extractLinesFromFile(fileName: String): Either[FileNotFound, List[String]] = {
    Either.catchNonFatal(Source.fromResource(fileName).getLines().toList).left.map(_ => FileNotFound(fileName))
  }

  private def mapFromBaseLine(baseLines: List[BaseLine]): Map[String, BaseLine] = {
    baseLines.map { baseLine => (baseLine.data, baseLine) }.toMap
  }

  def buildBaseLines(lines: List[String]): Either[DecisionTreeError, List[BaseLine]] = {
    val regexParent = """([0-9]+):\[(.*?)\] (yes)=([0-9]+),(no)=([0-9]+)""".r
    val regexLeaf = """([0-9]+):(leaf)=([+-]?[1-9][0-9]*|0[.,][0-9]+)""".r

    lines.map {
      case regexParent(data, strCond, _, yes, _, no) => Right(ParentLine(data, strCond, yes, no))
      case regexLeaf(data, _, strValue) => Right(LeafLine(data, strValue))
      case _ => Left(PatternNotFound)
    }.sequence.leftWiden[DecisionTreeError]
  }

  def buildRecursively(map: Map[String, BaseLine], rootLine: BaseLine): Either[DecisionTreeError, Node] = {
    rootLine match {
      case parentLine: ParentLine =>
        Right(build(map, parentLine))
      case leafLine: LeafLine => Left(MalformedError(leafLine.toString))
    }
  }


  def build(map: Map[String, BaseLine], baseLine: BaseLine): Node = {
    baseLine match {
      case parentLine: ParentLine =>
        val yes: Node = buildNode(map, parentLine.yes)
        val no: Node = buildNode(map, parentLine.no)
        ParentNode(parentLine.strCond, yes, no)

      case leafLine: LeafLine => LeafNode(leafLine.value)

      case _ => ???
    }
  }

  def buildNode(map: Map[String, BaseLine], index: String): Node = {
    val line = map(index)

    line match {
      case parentLine: ParentLine => build(map, parentLine)
      case leafLine: LeafLine => build(map, leafLine)
    }
  }


}