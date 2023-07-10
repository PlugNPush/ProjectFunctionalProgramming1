
// 1. Read the JSON file
// we start by importing the JSON library with ZIO
import zio.json._
import java.io._

case class Sudoku(
  sudoku: List[List[Option[Int]]]
)

def importSudokuFromJson(filePath: String): Option[Sudoku] = {
  val json = scala.io.Source.fromFile(filePath).mkString // we read the file

  // we create a parser for the JSON file
  val parser = DeriveJsonDecoder.gen[Sudoku]
  val result = parser.decodeJson(json).getOrElse(Sudoku(Nil))
  
  Some(result)
}

val json_file = 1
val filePath = s"grids/input/${json_file}.json"

val importedSudoku = importSudokuFromJson(filePath).getOrElse(Sudoku(Nil))

//we have in the json :
/*
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

// 2. Print the sudoku

// show the importedSudoku with design and getorelse with default value '0'
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
// Sudoku(List(List(Some(9), None, None, None, Some(7), None, Some(3), None, None), List(None, Some(1), Some(5), None, Some(2), None, None, Some(4), Some(6)), List(None, None, Some(8), Some(6), None, None, Some(2), Some(5), None), List(Some(4), Some(6), None, Some(1), Some(8), Some(2), None, None, None), List(None, Some(7), Some(9), None, None, None, Some(8), Some(3), None), List(None, None, None, Some(9), Some(3), Some(7), None, Some(6), Some(2)), List(None, Some(3), Some(7), None, None, Some(1), Some(5), None, None), List(Some(1), Some(8), None, None, Some(5), None, Some(6), Some(9), None), List(None, None, Some(4), None, Some(6), None, None, None, Some(3))))


def throwableVerifySudoku(sudoku: Sudoku): Unit = {
    val rows = sudoku.sudoku

    // Check if the sudoku has 9 rows
    if (rows.length != 9)
      throw new Exception("The sudoku does not have 9 rows")

    // Check if the sudoku has 9 columns for each row
    if (rows.exists(row => row.length != 9))
      throw new Exception("The sudoku does not have 9 columns for each row")

    // Check if the sudoku has only numbers between 1 and 9, or None
    if (rows.exists(row => row.exists(cell => cell.exists(value => value < 1 || value > 9))))
      throw new Exception("The sudoku has a number that is not between 1 and 9, or None")

    // Check for each line that the numbers are unique, except for None from 1 to 9
    if ((0 until 9).exists(row => 
      rows(row)
        .filter(_.isDefined) // Filter out None values
        .groupBy(cell => cell)
        .view
        .mapValues(_.size)
        .exists(_._2 > 1)))
      throw new Exception("The sudoku has a number that is not unique in a line")


    // Check for each column that the numbers are unique, except for None from 1 to 9
    if ((0 until 9).exists(col => 
      rows
        .map(_(col)) // Get the column
        .filter(_.isDefined) // Filter out None values
        .groupBy(cell => cell)
        .view
        .mapValues(_.size)
        .exists(_._2 > 1)))
      throw new Exception("The sudoku has a number that is not unique in a column")


    // Check for each square that the numbers are unique, except for None from 1 to 9
    if ((0 until 9).exists(square => {
      val regionRow = 3 * (square / 3)
      val regionCol = 3 * (square % 3)
      val region = (for {
        r <- 0 until 3
        c <- 0 until 3
      } yield (regionRow + r, regionCol + c)).map { case (r, c) => rows(r)(c) }
      region.filter(_.isDefined).groupBy(cell => cell).view.mapValues(_.size).exists(_._2 > 1)
    }))
      throw new Exception("The sudoku has a number that is not unique in a square")
    
  }

val status = throwableVerifySudoku(importedSudoku)
println("Verification done, the sudoku is valid")

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

printSudoku(importedSudoku)

// 3. Resolve the sudoku

//import scala.util.boundary, boundary.break <- need to use this instead of return

def solveSudoku(sudoku: Sudoku): Option[Sudoku] = {
  def isValid(sudoku: Sudoku, row: Int, col: Int, num: Int): Boolean = {

    // Check if the number is already present in the same row
    if ((0 until 9).exists(c => sudoku.sudoku(row)(c).contains(num)))
        return false

    // Check if the number is already present in the same column
    if ((0 until 9).exists(r => sudoku.sudoku(r)(col).contains(num)))
        return false

    // Check if the number is already present in the same square
    val regionRow = 3 * (row / 3)
    val regionCol = 3 * (col / 3)
    if ((for {
      r <- 0 until 3
      c <- 0 until 3
    } yield (regionRow + r, regionCol + c)).exists { case (r, c) => sudoku.sudoku(r)(c).contains(num) })
        return false

    true
}

  def solve(sudoku: Sudoku, row: Int, col: Int): Option[Sudoku] = {
    if (row == 9)
      Some(sudoku) // Sudoku resolved
    else if (col == 9)
      solve(sudoku, row + 1, 0)
    else sudoku.sudoku(row)(col) match {
      case Some(value) => solve(sudoku, row, col + 1) // Case already fill, go to the next case
      case None =>
        (1 to 9) // Range from 1 to 9
          .view // Lazy evaluation
          .map(num => if (isValid(sudoku, row, col, num)) { // We map to num and check if the number is valid
            val newSudoku = sudoku.copy(sudoku = sudoku.sudoku.updated(row, sudoku.sudoku(row).updated(col, Some(num)))) // Num is valid, we update the sudoku with it
            solve(newSudoku, row, col + 1) // We recursively call the function to continue to solve the sudoku for the next column
          } else None) // If num is not valid, we return None
          .find(_.isDefined) // We find the first defined value, or return None
          .flatten // We flatten the Option[Option[Sudoku]] to Option[Sudoku]
          .orElse(None) // We return None if no value is found after the flatten
    }
}


  solve(sudoku, 0, 0)
}

val solvedSudoku = solveSudoku(importedSudoku)
solvedSudoku.foreach(printSudoku)


// 4. Create a reverse parser into a JSON file

def exportSudokuToJson(sudoku: Sudoku, filePath: String): Unit = {
  val encoder = DeriveJsonEncoder.gen[Sudoku]
  val json = encoder.encodeJson(sudoku)
  val jsonString = json.toString

  val pw = new PrintWriter(new File(filePath))
  pw.write(jsonString)
  pw.close()
}

exportSudokuToJson(solvedSudoku.getOrElse(Sudoku(Nil)), s"grids/result/${json_file}.json")


// 5. Generate a new Sudoku

import scala.util.Random

def generateSudoku(): (Sudoku, Sudoku) = {
  val emptySudoku = Sudoku(List.fill(9)(List.fill(9)(None)))

  val solution = solveSudoku(emptySudoku).getOrElse(emptySudoku)

  val random = new Random()

  val filledCells = random.nextInt(17) + 17 // Generate a random number between 17 and 33
  var count = 0

  val sudokuWithHints = emptySudoku.copy(sudoku = solution.sudoku.map(row =>
    row.map(cell => {
      if (count < filledCells && random.nextBoolean()) {
        count += 1
        cell
      } else {
        None
      }
    })
  ))

  (sudokuWithHints, solution)
}

val (sudokuWithHints, solution) = generateSudoku()

println("Vérification du Sudoku avec des indices :")
throwableVerifySudoku(sudokuWithHints)
println("Vérification effectuée, le sudoku est valide")

println("Sudoku avec des indices :")
printSudoku(sudokuWithHints)

println("Solution du Sudoku :")
printSudoku(solution)

