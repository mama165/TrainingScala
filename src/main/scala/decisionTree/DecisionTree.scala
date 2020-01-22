package decisionTree

object DecisionTree extends App {

  sealed class BaseLine(data: String)

  case class ParentLine(data: String, strCond: String, yes: String, no: String) extends BaseLine(data: String)
  case class LeafLine(data: String, strValue: String) extends BaseLine(data: String)

  sealed class Node

  case class ParentNode(strCond: String, nodeYes: Option[Node], nodeNo: Option[Node]) extends Node

  case class LeafNode(value: String) extends Node

  val lines = List(
    "0:[device_type=pc||or||browser=7] yes=2,no=1",
    "2:[os_family=5] yes=6,no=5",
    "6:[browser=8] yes=12,no=11",
    "12:[language=2] yes=20,no=19",
    "20:leaf=0.000559453",
    "19:leaf=0.000594593",
    "11:[size=300x600] yes=18,no=17",
    "18:leaf=0.000597397",
    "17:leaf=0.00063461",
    "5:[browser=8||or||browser=5] yes=10,no=9",
    "10:leaf=0.000625534",
    "9:[position=2] yes=16,no=15",
    "16:leaf=0.00066727",
    "15:leaf=0.000708484",
    "1:[browser=8] yes=4,no=3",
    "4:leaf=0.000881108",
    "3:[os_family=5] yes=8,no=7",
    "8:leaf=0.000842268",
    "7:[region=FR:A5] yes=14,no=13",
    "14:leaf=0.000939982",
    "13:leaf=0.000999001",
  )

  val nodes = buildBaseLines(lines)

  def buildBaseLines(lines: List[String]): List[BaseLine] = {
    val regexParent = "([0-9]+):\\[(.*?)\\] (yes)=([0-9]+),(no)=([0-9]+)".r
    val regexLeaf = "([0-9]+):(leaf)=([+-]?[1-9][0-9]*|0[.,][0-9]+)".r

    lines.map {
      case regexParent(data, strCond, _, yes, _, no) => ParentLine(data, strCond, yes, no)
      case regexLeaf(data, _, strValue) => LeafLine(data, strValue)
      case _ => ???
    }
  }

  def buildNodes(baseLine: List[BaseLine]) : List[Node] = {
    
    ???
  }
}