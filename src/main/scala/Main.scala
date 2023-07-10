package sudoku

import zio._
import zio.json._
import java.io.PrintWriter, java.io.File
import zio.Config.Bool
import java.time.Instant

object Main extends ZIOAppDefault {

  case class Sudoku(
    sudoku: List[List[Option[Int]]]
  )

  def importSudokuFromJson(filePath: String): Option[Sudoku] = {
    try {
      val json = scala.io.Source.fromFile(filePath).mkString // we read the file

      // we create a parser for the JSON file
      val parser = DeriveJsonDecoder.gen[Sudoku]
      val result = parser.decodeJson(json).getOrElse(Sudoku(Nil))

      Some(result)
    } catch {
      case e: Exception => throw new Exception(s"Error while importing the sudoku from the json file (does the file exists?): ${e}"); None
    }
    
  }

  def throwableVerifySudoku(sudoku: Sudoku): Boolean = {
    val rows = sudoku.sudoku

    if (rows.isEmpty)
      throw new Exception("The sudoku property is empty or inexistent, are you sure you imported the right JSON file?")

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
      

    true
  }

  def printSudoku(sudoku: Sudoku): ZIO[Any, Throwable, Unit] = {
  val rows = sudoku.sudoku

  for {
    _ <- Console.printLine("+-------+-------+-------+")
    _ <- ZIO.foreach(0 until 9) { i =>
      for {
        _ <- if (i % 3 == 0 && i != 0) Console.printLine("+-------+-------+-------+") else ZIO.unit
        _ <- ZIO.foreach(0 until 9) { j =>
          for {
            _ <- if (j % 3 == 0) Console.print("| ") else ZIO.unit
            _ <- rows(i)(j) match {
              case Some(value) => Console.print(s"$value ")
              case None => Console.print("0 ")
            }
          } yield ()
        }
        _ <- Console.printLine("|")
      } yield ()
    }
    _ <- Console.printLine("+-------+-------+-------+")
  } yield ()
}

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

  def exportSudokuToJson(sudoku: Sudoku, filePath: String) = {
    val encoder = DeriveJsonEncoder.gen[Sudoku]
    val json = encoder.encodeJson(sudoku)
    val jsonString = json.toString

    val pw = new PrintWriter(new File(filePath))
    pw.write(jsonString)
    pw.close()
}

  def run: ZIO[Any, Throwable, Unit] =
    val filePath: String = ""
    val importedSudoku: Sudoku = Sudoku(Nil)
    val start: Long = 0
    val solvedSudoku: Sudoku = Sudoku(Nil)
    val end: Long = 0
    val time: Long = 0
    for {
      _ <- Console.print("Enter the path to the JSON filename containing the Sudoku problem (the file should be placed in ./grids/input/): ")
      path <- Console.readLine
      filePath = s"grids/input/${path}"
      _ <- Console.printLine(s"Checking file: $filePath")

      importedSudoku = importSudokuFromJson(filePath).getOrElse(throw new Exception("Error while importing the Sudoku from the JSON file"))
      _ <- Console.printLine("Sudoku imported successfully")

      _ <- Console.printLine("Checking the Sudoku for validity")
      sudokuStatus = throwableVerifySudoku(importedSudoku)
      _ <- Console.printLine("Sudoku is valid")

      _ <- Console.printLine("Printing the Sudoku")
      _ <- printSudoku(importedSudoku)

      _ <- Console.printLine("Solving the Sudoku")
      start = Instant.now().toEpochMilli()
      solvedSudoku = solveSudoku(importedSudoku).getOrElse(throw new Exception("Error while solving the Sudoku (is it solvable?)"))
      end = Instant.now().toEpochMilli()
      time = end - start
      _ <- Console.printLine(s"Sudoku solved in $time ms")

      _ <- Console.printLine("Printing the solved Sudoku")
      _ <- printSudoku(solvedSudoku)

      _ <- Console.printLine("Exporting the solved Sudoku to JSON")
      _ = exportSudokuToJson(solvedSudoku, s"grids/result/${path}")
      _ <- Console.printLine(s"Sudoku exported successfully (saved to ./grids/result/${path})")


      // Add your Sudoku solver logic here, utilizing ZIO and interacting with the ZIO Console
    } yield ()
}