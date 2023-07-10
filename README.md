# Class Exam Instruction: Sudoku Solver in Scala

![current tests status](https://github.com/plugnpush/projectfunctionalprogramming1/actions/workflows/scala.yml/badge.svg)

# Team Members
Vicent MARGUET, Logan LE LAY, Michaël NASS, Loïc TRÉHIN
M1-SE2

# Initialize

If you open this project in VSCode, you can validate the import build and metal pop-up and then run the following command in the terminal:

```bash
sbt
test # in sbt shell
run # in sbt shell
```

# Test and run

```bash
sbt test
sbt run
```
All the tests should succeed before attempting to run the program. Make sure you included all the input and test grids from this repository in your copy of the project to allow the tests to run.

# About the tests
When running the tests, you should see all the following lines in green from your terminal.
```text
sbt:sudoku-solver> test
[info] compiling 1 Scala source to /Users/plugn/Downloads/ProjectFunctionalProgramming1/sudoku-solver/target/scala-3.3.0/test-classes ...

sudoku.MySuite:
  + Solve a sudoku #1 should succeed 0.045s
  + Solve a sudoku #2 should succeed 0.003s
  + Solve a sudoku #3 should succeed 0.076s
  + Solve a sudoku #4 should succeed 0.004s
  + Solve a sudoku #5 should succeed 0.002s
  + Comparing sudoku #1 resolution against test #5 should not be equal 0.001s
  + Loading an inexistent sudoku should throw an exception 0.0s
  + Sudoku inv_1 should be invalid (empty due to invalid json object name) 0.001s
  + Sudoku inv_2 should be invalid (row size mismatch) 0.0s
  + Sudoku inv_3 should be invalid (column size mismatch) 0.0s
  + Sudoku inv_4 should be invalid (content values outside bounds 1-9) 0.001s
  + Sudoku inv_5 should be invalid (duplicate in row) 0.0s
  + Sudoku inv_6 should be invalid (duplicate in column) 0.0s
  + Sudoku inv_7 should be invalid (duplicate in square) 0.001s
  + Sudoku inv_8 should be invalid (empty due invalid character) 0.0s
  + Saving the solution from sudoku #1 should succeed 0.023s
  + Saving the solution from sudoku #2 should succeed 0.002s
  + Saving the solution from sudoku #3 should succeed 0.082s
  + Saving the solution from sudoku #4 should succeed 0.003s
  + Saving the solution from sudoku #5 should succeed 0.002s
[info] Passed: Total 20, Failed 0, Errors 0, Passed 20
[success] Total time: 1 s, completed Jul 10, 2023, 3:43:05 AM
sbt:sudoku-solver> 
```
You can also check that the current build of the project is operational by going to the Actions tab of the GitHub repository and monitor the GitHub CI Action for Scala. Every time a modification is being pushed to the GitHub repository, an automated CI pipeline runs the tests and reports on the results. The current build status is: ![current tests status](https://github.com/plugnpush/projectfunctionalprogramming1/actions/workflows/scala.yml/badge.svg).

# How to use the Sudoku Solver

Place your grids in ./grids/input in JSON format.
The following structure is required:

```json
{
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
```
Each line of your grid is represented in an array, and all of them are a component of a grid array named "sudoku".
All the empty values should be set with null. <b>DO NOT SET ZEROES AS THE GRID WILL FAIL THE VALIDITY TESTS, even though the empty values will be represented by zeroes when being printed on the screen.</b>

When you run the project, you will be prompted using the ZIO console to enter the filename.
If your file is saved in ./grids/input/32.json, you should type 32.json and press enter.
Loading files outside of ./grids/input/ is not supported in this version for convenience purposes, as well as loading files from subfolders.
Loading files from a subfolder like ./grids/input/easy/32.json will result in a crash on the solution export, unless you create the easy subfolder in the ./grids/result folder as well.

The program will parse your JSON file and proceed to a quality check of your grid.
The program is designed to throw an error if a step files, and will be providing explainations about what went wrong.

Once your grid passed the quality check, the solver will find a solution using the dedicated recursive algorithm.
The algorithm works as follows:
```scala
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
```

Once your sudoku is solved, it will be displayed on the screen and it will be saved in ./grids/result using the same filename you provided when starting the program.

# Demonstration using the Console

Here is a demonstration of the console when running the program with the file "1.json":
```bash
sbt:sudoku-solver> run
[info] running sudoku.Main 
Enter the path to the JSON filename containing the Sudoku problem (the file should be placed in ./grids/input/): 1.json
Checking file: grids/input/1.json
Sudoku imported successfully
Checking the Sudoku for validity
Sudoku is valid
Printing the Sudoku
+-------+-------+-------+
| 9 0 0 | 0 7 0 | 3 0 0 |
| 0 1 5 | 0 2 0 | 0 4 6 |
| 0 0 8 | 6 0 0 | 2 5 0 |
+-------+-------+-------+
| 4 6 0 | 1 8 2 | 0 0 0 |
| 0 7 9 | 0 0 0 | 8 3 0 |
| 0 0 0 | 9 3 7 | 0 6 2 |
+-------+-------+-------+
| 0 3 7 | 0 0 1 | 5 0 0 |
| 1 8 0 | 0 5 0 | 6 9 0 |
| 0 0 4 | 0 6 0 | 0 0 3 |
+-------+-------+-------+
Solving the Sudoku
Sudoku solved in 1 ms
Printing the solved Sudoku
+-------+-------+-------+
| 9 2 6 | 4 7 5 | 3 1 8 |
| 3 1 5 | 8 2 9 | 7 4 6 |
| 7 4 8 | 6 1 3 | 2 5 9 |
+-------+-------+-------+
| 4 6 3 | 1 8 2 | 9 7 5 |
| 2 7 9 | 5 4 6 | 8 3 1 |
| 8 5 1 | 9 3 7 | 4 6 2 |
+-------+-------+-------+
| 6 3 7 | 2 9 1 | 5 8 4 |
| 1 8 2 | 3 5 4 | 6 9 7 |
| 5 9 4 | 7 6 8 | 1 2 3 |
+-------+-------+-------+
Exporting the solved Sudoku to JSON
Sudoku exported successfully (saved to ./grids/result/1.json)
[success] Total time: 2 s, completed Jul 10, 2023, 3:21:23 AM
sbt:sudoku-solver> 
```

# List of the exceptions in the Sudoku Solver

Here are all the exception possibilities (they all extend Exception):
  - Error while importing the sudoku from the json file (does the file exists?) (SudokuImportFailure, IOException)
  - Error while importing the Sudoku from the JSON file (SudokuImportFailure)
  - The sudoku property is empty or inexistent, are you sure you imported the right JSON file? (the JSON parser likely failed to comply with Sodoku type) (EmptySudoku, InvalidSudoku)
  - The sudoku does not have 9 rows (InvalidSudokuRowSize, InvalidSudoku)
  - The sudoku does not have 9 columns for each row (InvalidSudokuColumnSize, InvalidSudoku)
  - The sudoku has a number that is not between 1 and 9, or None (InvalidSudokuContent, InvalidSudoku)
  - The sudoku has a number that is not unique in a line (InvalidSudokuLine, InvalidSudoku)
  - The sudoku has a number that is not unique in a column (InvalidSudokuColumn, InvalidSudoku)
  - The sudoku has a number that is not unique in a square (InvalidSudokuSquare, InvalidSudoku)
  - Error while solving the Sudoku (is it solvable?) (SudokuSolveError)
  - Error while exporting the Sudoku to the JSON file (SudokuExportFailure, IOException)


