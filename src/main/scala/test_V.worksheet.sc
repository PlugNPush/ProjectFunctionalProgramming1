
// 1. Read the JSON file
// we start by importing the JSON library with ZIO
import zio.json._

// we read the file from grids/input/1.json
val json = scala.io.Source.fromFile("grids/input/1.json").mkString

/* //we have :
    {
    "sudoku": {
      "1": [[9, null, null], [null, 1, 5], [null, null, 8]],
      "2": [[null, 7, null], [null, 2, null], [6, null, null]],
      "3": [[3, null, null], [null, 4, 6], [2, 5, null]],
      "4": [[4, 6, null], [null, 7, 9], [null, null, null]],
      "5": [[1, 8, 2], [null, null, null], [9, 3, 7]],
      "6": [[null, null, null], [8, 3, null], [null, 6, 2]],
      "7": [[null, 3, 7], [1, 8, null], [null, null, 4]],
      "8": [[null, null, 1], [null, 5, null], [null, 6, null]],
      "9": [[5, null, null], [6, 9, null], [null, null, 3]]
    }
}
*/

// 2. Create a parser for the JSON file
 
case class Sudoku(
  sudoku: Map[String, List[List[Option[Int]]]]
)

case class Sudoku_traitment(
  sudoku: Either[String, Seq[(String, List[List[Option[Int]]])]]
)

// we create a parser for the JSON file
val parser = DeriveJsonDecoder.gen[Sudoku]
// sort the result by the key in ascending order
val result = parser.decodeJson(json).map(_.sudoku.toSeq.sortBy(_._1.toInt))
print(result)

// show the result with design and getorelse with default value '0'
// we want to obtain a result like this :
// +-------+-------+-------+
// + 9 0 0 + 0 7 0 + 3 0 0 +
// + 0 1 5 + 0 2 0 + 0 4 6 +
// + 0 0 8 + 6 0 0 + 2 5 0 +
// +-------+-------+-------+
// + 4 6 0 + 1 8 2 + 0 0 0 +
// + 0 7 9 + 0 0 0 + 8 3 0 +
// + 0 0 0 + 9 3 7 + 0 6 2 +
// +-------+-------+-------+
// + 0 3 7 + 0 0 1 + 5 0 0 +
// + 1 8 0 + 0 5 0 + 6 9 0 +
// + 0 0 4 + 0 6 0 + 0 0 3 +
// +-------+-------+-------+


def showResult0(result: Either[String, Seq[(String, List[List[Option[Int]]])]]) = {
  result match {
    case Left(error) => println(error)
    case Right(sudoku) => sudoku.foreach { case (key, value) =>
      println(s"Grid $key")
      value.foreach { row =>
        println(row.map(_.getOrElse(0)).mkString(" "))
      }
      println()
    }
  }
}

// we show the result
showResult0(result)

// we flatten the result
val result2 = result.map(_.flatMap(_._2))

// we want now to show the result like this where numbers correspond to the list index of result2 :
// +---+---+---+
// + 1 + 4 + 7 +
// + 2 + 5 + 8 +
// + 3 + 6 + 9 +
// +---+---+---+
// + 10 + 13 + 16 +
// + 11 + 14 + 17 +
// + 12 + 15 + 18 +
// +---+---+---+
// ...

result2 match {
  case Left(error) => println(error)
  case Right(sudoku) => {
    println("+-------+-------+-------+")
    var j = 0
    for (i <- 0 to 8) {
      print("+ ")
    //print(s"${sudoku(i+(j/3).toInt)(currentj).map(_.getOrElse(0)).mkString(" ")} ")
        print(s"${sudoku(i+j).map(_.getOrElse(0)).mkString(" ")} | ${sudoku(i+3+j).map(_.getOrElse(0)).mkString(" ")} | ${sudoku(i+6+j).map(_.getOrElse(0)).mkString(" ")} ")

      println("+")
      if (i == 2 || i == 5) {println("+-------+-------+-------+"); j += 6}
    }
    println("+-------+-------+-------+")
  }
}

// 3. Create a reverse parser into a JSON file
// to reverse result2 we need to readapt it to a Sudoku case class so reverse the result.map(_.flatMap(_._2))
// we want to obtain a result like this :
// Right(List((1,List(List(Some(9), None, None), List(None, Some(1), Some(5)), List(None, None, Some(8)))), (2,List(List(None, Some(7), None), List(None, Some(2), None), List(Some(6), None, None))), (3,List(List(Some(3), None, None), List(None, Some(4), Some(6)), List(Some(2), Some(5), None))), (4,List(List(Some(4), Some(6), None), List(None, Some(7), Some(9)), List(None, None, None))), (5,List(List(Some(1), Some(8), Some(2)), List(None, None, None), List(Some(9), Some(3), Some(7)))), (6,List(List(None, None, None), List(Some(8), Some(3), None), List(None, Some(6), Some(2)))), (7,List(List(None, Some(3), Some(7)), List(Some(1), Some(8), None), List(None, None, Some(4)))), (8,List(List(None, None, Some(1)), List(None, Some(5), None), List(None, Some(6), None))), (9,List(List(Some(5), None, None), List(Some(6), Some(9), None), List(None, None, Some(3))))))
// and we have this :
// Right(List(List(Some(9), None, None), List(None, Some(1), Some(5)), List(None, None, Some(8)), List(None, Some(7), None), List(None, Some(2), None), List(Some(6), None, None), List(Some(3), None, None), List(None, Some(4), Some(6)), List(Some(2), Some(5), None), List(Some(4), Some(6), None), List(None, Some(7), Some(9)), List(None, None, None), List(Some(1), Some(8), Some(2)), List(None, None, None), List(Some(9), Some(3), Some(7)), List(None, None, None), List(Some(8), Some(3), None), List(None, Some(6), Some(2)), List(None, Some(3), Some(7)), List(Some(1), Some(8), None), List(None, None, Some(4)), List(None, None, Some(1)), List(None, Some(5), None), List(None, Some(6), None), List(Some(5), None, None), List(Some(6), Some(9), None), List(None, None, Some(3))))

// so we need to group the result by 3 and then add a key to each list :
// i have Either[String, List[(Int, Seq[List[Option[Int]]])]]
// i want Map[String, List[List[Option[Int]]]]

// we transform the result3 to a JSON format :
    // DeriveJsonDecoder.gen[Sudoku] then parser.decodeJson(json) // to decode a JSON
    // DeriveJsonEncoder.gen[Sudoku] then parser.encodeJson(sudoku) // to encode a JSON
val parser2 = DeriveJsonEncoder.gen[Either[String, Seq[(String, List[List[Option[Int]]])]]]
val result4 = parser2.encodeJson(result)

// i can now write the result in a JSON file :
import java.io._
val pw = new PrintWriter(new File("grids/result/1.json" ))
pw.write(result4.toString)
pw.close
