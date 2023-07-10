// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html

package sudoku

class MySuite extends munit.FunSuite {
  test("Solve a sudoku #1 should succeed") {
    val sudoku = Main.importSudokuFromJson("grids/input/1.json").getOrElse(fail("Could not import sudoku"))
    Main.throwableVerifySudoku(sudoku)
    val solvedSudoku = Main.solveSudoku(sudoku).getOrElse(fail("Could not solve sudoku"))
    val actualSolution = Main.importSudokuFromJson("grids/test/1.json").getOrElse(fail("Could not import solution"))
    assertEquals(solvedSudoku.sudoku, actualSolution.sudoku)
  }

  test("Solve a sudoku #2 should succeed") {
    val sudoku = Main.importSudokuFromJson("grids/input/2.json").getOrElse(fail("Could not import sudoku"))
    Main.throwableVerifySudoku(sudoku)
    val solvedSudoku = Main.solveSudoku(sudoku).getOrElse(fail("Could not solve sudoku"))
    val actualSolution = Main.importSudokuFromJson("grids/test/2.json").getOrElse(fail("Could not import solution"))
    assertEquals(solvedSudoku.sudoku, actualSolution.sudoku)
  }

  test("Solve a sudoku #3 should succeed") {
    val sudoku = Main.importSudokuFromJson("grids/input/3.json").getOrElse(fail("Could not import sudoku"))
    Main.throwableVerifySudoku(sudoku)
    val solvedSudoku = Main.solveSudoku(sudoku).getOrElse(fail("Could not solve sudoku"))
    val actualSolution = Main.importSudokuFromJson("grids/test/3.json").getOrElse(fail("Could not import solution"))
    assertEquals(solvedSudoku.sudoku, actualSolution.sudoku)
  }

  test("Solve a sudoku #4 should succeed") {
    val sudoku = Main.importSudokuFromJson("grids/input/4.json").getOrElse(fail("Could not import sudoku"))
    Main.throwableVerifySudoku(sudoku)
    val solvedSudoku = Main.solveSudoku(sudoku).getOrElse(fail("Could not solve sudoku"))
    val actualSolution = Main.importSudokuFromJson("grids/test/4.json").getOrElse(fail("Could not import solution"))
    assertEquals(solvedSudoku.sudoku, actualSolution.sudoku)
  }

  test("Solve a sudoku #5 should succeed") {
    val sudoku = Main.importSudokuFromJson("grids/input/5.json").getOrElse(fail("Could not import sudoku"))
    Main.throwableVerifySudoku(sudoku)
    val solvedSudoku = Main.solveSudoku(sudoku).getOrElse(fail("Could not solve sudoku"))
    val actualSolution = Main.importSudokuFromJson("grids/test/5.json").getOrElse(fail("Could not import solution"))
    assertEquals(solvedSudoku.sudoku, actualSolution.sudoku)
  }

  test("Comparing sudoku #1 resolution against test #5 should not be equal") {
    val sudoku = Main.importSudokuFromJson("grids/input/1.json").getOrElse(fail("Could not import sudoku"))
    Main.throwableVerifySudoku(sudoku)
    val solvedSudoku = Main.solveSudoku(sudoku).getOrElse(fail("Could not solve sudoku"))
    val actualSolution = Main.importSudokuFromJson("grids/test/5.json").getOrElse(fail("Could not import solution"))
    assertNotEquals(solvedSudoku.sudoku, actualSolution.sudoku)
  }

  test("Loading an inexistent sudoku should throw an exception") {
    intercept[Exception] {
      val sudoku = Main.importSudokuFromJson("grids/input/does_not_exists.json")
    }
  }

  test("Sudoku inv_1 should be invalid") {
    val sudoku = Main.importSudokuFromJson("grids/input/inv_1.json").getOrElse(fail("Could not import sudoku"))
    intercept[Exception] {
      Main.throwableVerifySudoku(sudoku)
    }
  }

  test("Sudoku inv_2 should be invalid") {
    val sudoku = Main.importSudokuFromJson("grids/input/inv_2.json").getOrElse(fail("Could not import sudoku"))
    intercept[Exception] {
      Main.throwableVerifySudoku(sudoku)
    }
  }

  test("Sudoku inv_3 should be invalid") {
    val sudoku = Main.importSudokuFromJson("grids/input/inv_3.json").getOrElse(fail("Could not import sudoku"))
    intercept[Exception] {
      Main.throwableVerifySudoku(sudoku)
    }
  }

  test("Sudoku inv_4 should be invalid") {
    val sudoku = Main.importSudokuFromJson("grids/input/inv_4.json").getOrElse(fail("Could not import sudoku"))
    intercept[Exception] {
      Main.throwableVerifySudoku(sudoku)
    }
  }

  test("Sudoku inv_5 should be invalid") {
    val sudoku = Main.importSudokuFromJson("grids/input/inv_5.json").getOrElse(fail("Could not import sudoku"))
    intercept[Exception] {
      Main.throwableVerifySudoku(sudoku)
    }
  }

  test("Sudoku inv_6 should be invalid") {
    val sudoku = Main.importSudokuFromJson("grids/input/inv_6.json").getOrElse(fail("Could not import sudoku"))
    intercept[Exception] {
      Main.throwableVerifySudoku(sudoku)
    }
  }

  test("Sudoku inv_7 should be invalid") {
    val sudoku = Main.importSudokuFromJson("grids/input/inv_7.json").getOrElse(fail("Could not import sudoku"))
    intercept[Exception] {
      Main.throwableVerifySudoku(sudoku)
    }
  }

  test("Saving the solution from sudoku #1 should succeed") {
    val sudoku = Main.importSudokuFromJson("grids/input/1.json").getOrElse(fail("Could not import sudoku"))
    Main.throwableVerifySudoku(sudoku)
    val solvedSudoku = Main.solveSudoku(sudoku).getOrElse(fail("Could not solve sudoku"))
    Main.exportSudokuToJson(solvedSudoku, "grids/result/1.json")
    val actualSolution = Main.importSudokuFromJson("grids/result/1.json").getOrElse(fail("Could not import solution"))
    Main.throwableVerifySudoku(actualSolution)
    assertEquals(solvedSudoku.sudoku, actualSolution.sudoku)
  }

  test("Saving the solution from sudoku #2 should succeed") {
    val sudoku = Main.importSudokuFromJson("grids/input/2.json").getOrElse(fail("Could not import sudoku"))
    Main.throwableVerifySudoku(sudoku)
    val solvedSudoku = Main.solveSudoku(sudoku).getOrElse(fail("Could not solve sudoku"))
    Main.exportSudokuToJson(solvedSudoku, "grids/result/2.json")
    val actualSolution = Main.importSudokuFromJson("grids/result/2.json").getOrElse(fail("Could not import solution"))
    Main.throwableVerifySudoku(actualSolution)
    assertEquals(solvedSudoku.sudoku, actualSolution.sudoku)
  }

  test("Saving the solution from sudoku #3 should succeed") {
    val sudoku = Main.importSudokuFromJson("grids/input/3.json").getOrElse(fail("Could not import sudoku"))
    Main.throwableVerifySudoku(sudoku)
    val solvedSudoku = Main.solveSudoku(sudoku).getOrElse(fail("Could not solve sudoku"))
    Main.exportSudokuToJson(solvedSudoku, "grids/result/3.json")
    val actualSolution = Main.importSudokuFromJson("grids/result/3.json").getOrElse(fail("Could not import solution"))
    Main.throwableVerifySudoku(actualSolution)
    assertEquals(solvedSudoku.sudoku, actualSolution.sudoku)
  }

  test("Saving the solution from sudoku #4 should succeed") {
    val sudoku = Main.importSudokuFromJson("grids/input/4.json").getOrElse(fail("Could not import sudoku"))
    Main.throwableVerifySudoku(sudoku)
    val solvedSudoku = Main.solveSudoku(sudoku).getOrElse(fail("Could not solve sudoku"))
    Main.exportSudokuToJson(solvedSudoku, "grids/result/4.json")
    val actualSolution = Main.importSudokuFromJson("grids/result/4.json").getOrElse(fail("Could not import solution"))
    Main.throwableVerifySudoku(actualSolution)
    assertEquals(solvedSudoku.sudoku, actualSolution.sudoku)
  }

  test("Saving the solution from sudoku #5 should succeed") {
    val sudoku = Main.importSudokuFromJson("grids/input/5.json").getOrElse(fail("Could not import sudoku"))
    Main.throwableVerifySudoku(sudoku)
    val solvedSudoku = Main.solveSudoku(sudoku).getOrElse(fail("Could not solve sudoku"))
    Main.exportSudokuToJson(solvedSudoku, "grids/result/5.json")
    val actualSolution = Main.importSudokuFromJson("grids/result/5.json").getOrElse(fail("Could not import solution"))
    Main.throwableVerifySudoku(actualSolution)
    assertEquals(solvedSudoku.sudoku, actualSolution.sudoku)
  }

}
