
// 1. Read the JSON file
// we start by importing the JSON library with ZIO
import zio.json._

// we read the file from grids/input/1.json
val json = scala.io.Source.fromFile("grids/input/1.json").mkString

/* //we have :
json: String = {
  "sudoku": [
    [9, null, null, null, 7, null, 3, null, null],
    [null, 1, 5, null, 2, null, null, 4, 6],
    [null, null, 8, 6, null, null, 2, 5, null],
    [4, 6, null, 1, 8, 2, null, null, null],
    [null, 7, 9, null, null, null, 8, 3, null],
    [null, null, null, 9, 3, 7, null, 6, 2],
    [null, 3, 7, null, null, 1, 5, null, null],
    [1, 8, null, null, 5, null, 6, 9, null],
    [null, null, 4, null, 6, null, null, null, 3]
  ]
}
*/

// 2. Create a parser for the JSON file
 
case class Sudoku(
  sudoku: List[List[Option[Int]]]
)

// we create a parser for the JSON file
val parser = DeriveJsonDecoder.gen[Sudoku]
// sort the result by the key in ascending order
val result = parser.decodeJson(json).getOrElse(Sudoku(Nil))
print(result)

// show the result with design and getorelse with default value '0'
// we want to obtain a result like this :
/*
+-------+-------+-------+
+ 9 0 0 | 0 7 0 | 3 0 0 +
+ 0 1 5 | 0 2 0 | 0 4 6 +
+ 0 0 8 | 6 0 0 | 2 5 0 +
+-------+-------+-------+
+ 4 6 0 | 1 8 2 | 0 0 0 +
+ 0 7 9 | 0 0 0 | 8 3 0 +
+ 0 0 0 | 9 3 7 | 0 6 2 +
+-------+-------+-------+
+ 0 3 7 | 0 0 1 | 5 0 0 +
+ 1 8 0 | 0 5 0 | 6 9 0 +
+ 0 0 4 | 0 6 0 | 0 0 3 +
+-------+-------+-------+
*/
// and we have :
// Right(Sudoku(List(List(Some(9), None, None, None, Some(7), None, Some(3), None, None), List(None, Some(1), Some(5), None, Some(2), None, None, Some(4), Some(6)), List(None, None, Some(8), Some(6), None, None, Some(2), Some(5), None), List(Some(4), Some(6), None, Some(1), Some(8), Some(2), None, None, None), List(None, Some(7), Some(9), None, None, None, Some(8), Some(3), None), List(None, None, None, Some(9), Some(3), Some(7), None, Some(6), Some(2)), List(None, Some(3), Some(7), None, None, Some(1), Some(5), None, None), List(Some(1), Some(8), None, None, Some(5), None, Some(6), Some(9), None), List(None, None, Some(4), None, Some(6), None, None, None, Some(3)))))

def printSudoku(sudoku: Sudoku): Unit = {
  val rows = sudoku.sudoku

  println("+-------+-------+-------+")
  for (i <- 0 until 9) {
    if (i % 3 == 0 && i != 0) {
      println("+-------+-------+-------+")
    }
    for (j <- 0 until 9) {
      if (j % 3 == 0) {
        print("| ")
      }
      val cell = rows(i)(j)
      cell match {
        case Some(value) => print(s"$value ")
        case None => print("0 ")
      }
    }
    println("|")
  }
  println("+-------+-------+-------+")
}

printSudoku(result)


// 3. Create a reverse parser into a JSON file

val parser2 = DeriveJsonEncoder.gen[Sudoku]
val result2 = parser2.encodeJson(result)

// i can now write the result in a JSON file :
import java.io._
val pw = new PrintWriter(new File("grids/result/1.json" ))
pw.write(result2.toString)
pw.close
