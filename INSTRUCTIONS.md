# Class Exam Instruction: Sudoku Solver in Scala

## sbt project compiled with Scala 3

### Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

For more information on the sbt-dotty plugin, see the
[scala3-example-project](https://github.com/scala/scala3-example-project/blob/main/README.md).


## Topic

Write a Sudoku solver in Scala 3 using [ZIO](https://zio.dev/overview/getting-started), a purely functional, type-safe, composable library for asynchronous, concurrent programming in Scala. You will find useful to use additional libraries from the ZIP ecosystem, such as [`zio-nio`](https://zio.dev/zio-nio/) and [`zio-json`](https://zio.dev/zio-json/).

## Description

Sudoku is a popular logic-based puzzle game played on a 9x9 grid. The grid is divided into 9 rows, 9 columns, and 9 3x3 sub-grids. The objective is to fill the grid with digits from 1 to 9 such that each row, each column, and each sub-grid contains all the digits from 1 to 9 without any repetition. A partially filled Sudoku grid is given as input, and the solver should determine a solution if one exists.

### Unresolved Sudoku Grid

```
+-------+-------+-------+
| 5 3 0 | 0 7 0 | 0 0 0 |
| 6 0 0 | 1 9 5 | 0 0 0 |
| 0 9 8 | 0 0 0 | 0 6 0 |
+-------+-------+-------+
| 8 0 0 | 0 6 0 | 0 0 3 |
| 4 0 0 | 8 0 3 | 0 0 1 |
| 7 0 0 | 0 2 0 | 0 0 6 |
+-------+-------+-------+
| 0 6 0 | 0 0 0 | 2 8 0 |
| 0 0 0 | 4 1 9 | 0 0 5 |
| 0 0 0 | 0 8 0 | 0 7 9 |
+-------+-------+-------+
```

### Solution

```
+-------+-------+-------+
| 5 3 4 | 6 7 8 | 9 1 2 |
| 6 7 2 | 1 9 5 | 3 4 8 |
| 1 9 8 | 3 4 2 | 5 6 7 |
+-------+-------+-------+
| 8 5 9 | 7 6 1 | 4 2 3 |
| 4 2 6 | 8 5 3 | 7 9 1 |
| 7 1 3 | 9 2 4 | 8 5 6 |
+-------+-------+-------+
| 9 6 1 | 5 3 7 | 2 8 4 |
| 2 8 7 | 4 1 9 | 6 3 5 |
| 3 4 5 | 2 8 6 | 1 7 9 |
+-------+-------+-------+
```


## Expectations

1. Data Structure: Design an appropriate data structure to represent the Sudoku problem and solution. Ensure the data structure supports immutability and functional programming principles where possible. You have adopt an iterative approach with solving the problem first and then improving the data structure and/or the other application features.

1. ZIO Console Interaction: The solver should interact with the ZIO Console by requesting the user to provide a JSON file path containing a Sudoku problem. Upon receiving the path, the solver should attempt to solve the Sudoku puzzle and display the solution, if it exists.

1. Error Handling: Implement proper error handling mechanisms using ZIO's error handling capabilities. Handle scenarios such as invalid file paths, malformed JSON files, unsolvable Sudoku puzzles, etc. Provide informative error messages to the user.

1. Git Repository: Create a Git repository to manage your Sudoku solver project. Commit your code regularly and maintain a well-structured project organization.

1. Documentation Quality: Provide clear and concise documentation explaining the purpose, functionality, and usage of your Sudoku solver. Include instructions on how to run the solver and any additional setup required. Document any external libraries used and their significance in the project.

1. Recursive Approach: Implement the Sudoku solver using a recursive approach. Utilize the functional programming concepts available in Scala to write clean and readable recursive code.

1. Testing: Write appropriate tests to validate the correctness of your Sudoku solver implementation. Include test cases that cover various scenarios, such as empty grids, partially filled grids, invalid Sudoku puzzles, and solvable puzzles.

1. Functional Properties: Whenever possible, leverage the functional programming principles such as immutability, pure functions, and referential transparency. Aim to write code that is easy to reason about, test, and maintain.

## Additional Requirements

1. Group Size: Form groups of up to 4 students. You are encouraged to collaborate and discuss ideas within your group but ensure that each member actively contributes to the project.

1. Due Date: The project is expected to be completed within one week after the class. Submit your project by the specified due date and time. Late submissions may incur penalties unless prior arrangements have been made with the instructor.

1. Language: Use English for code, comments and documentation.

## Deliverables

1. Scala 3 code implementing the Sudoku solver using ZIO, adhering to the given requirements and expectations.

1. Git repository containing your code with appropriate commits and a README file providing instructions on how to run and test your Sudoku solver and the decisions made (librairies, data structure(s), algorithm and its performance, ...).

1. Documentation explaining the purpose, functionality, and usage of your Sudoku solver, along with any external libraries used.

## Grading

Your solution will be graded based on the following criteria, with equal distribution of the number of points on **criteria 1 to 5**:

1. Correctness and functionality of the Sudoku solver implementation.

1. Quality the data model, adherence to functional programming principles, and use of recursion.

1. Effective usage of ZIO, including error handling and ZIO Console interaction.

1. Testing completeness and effectiveness, covering various scenarios.

1. Quality and clarity of code organization and documentation, including the README file.

1. Collaboration within the group and active participation of each member.

1. Timely submission of the project by the specified due date.

## Additional Information

To get started with your Scala 3 project using sbt, follow these steps:

1. Install Coursier or review your steup by following the instructions provided at https://docs.scala-lang.org/getting-started/index.html.

1. Open a terminal or command prompt and navigate to the directory where you want to create your project.

1. Run the following command to generate a new Scala 3 project using sbt: `sbt new scala/scala3.g8`

1. Follow the prompts to provide a name and other details for your project.

Once your project is generated, you will have a basic project structure with a `build.sbt` file. Replace the contents of the `build.sbt` file with the following skeleton, including the mentioned libraries (latest versions):

```scala
val zioVersion = "2.0.15"
val scala3Version = "3.3.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "sudoku-solver",
    version := "1.0",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      // Add other libraries like zio-nio and zip-json here if needed
    ).map(_ % Compile),
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29"
    ).map(_ % Test)
  )
```

For the ZIO application template, replace the contents of the `src/main/scala/Main.scala` file with the following code:

```scala
package sudoku

import zio._

object Main extends ZIOAppDefault {

  def run: ZIO[Any, Throwable, Unit] =
    for {
      _ <- Console.print("Enter the path to the JSON file containing the Sudoku problem:")
      path <- Console.readLine
      _ <-  Console.printLine(s"You entered: $path")
      // Add your Sudoku solver logic here, utilizing ZIO and interacting with the ZIO Console
    } yield ()
}
```

This provides a basic ZIO application template that prompts the user for a JSON file path and prints the entered path. Modify this template to include your Sudoku solver logic using ZIO.

A basic interaction with the Sudoku solver in the ZIO Console could be :

```
Please enter a file: problem-001.json
Successfully parsed and validated JSON input
+-------+-------+-------+
| 5 3 0 | 0 7 0 | 0 0 0 |
| 6 0 0 | 1 9 5 | 0 0 0 |
| 0 9 8 | 0 0 0 | 0 6 0 |
+-------+-------+-------+
| 8 0 0 | 0 6 0 | 0 0 3 |
| 4 0 0 | 8 0 3 | 0 0 1 |
| 7 0 0 | 0 2 0 | 0 0 6 |
+-------+-------+-------+
| 0 6 0 | 0 0 0 | 2 8 0 |
| 0 0 0 | 4 1 9 | 0 0 5 |
| 0 0 0 | 0 8 0 | 0 7 9 |
+-------+-------+-------+
Resolving problem...
+-------+-------+-------+
| 5 3 4 | 6 7 8 | 9 1 2 |
| 6 7 2 | 1 9 5 | 3 4 8 |
| 1 9 8 | 3 4 2 | 5 6 7 |
+-------+-------+-------+
| 8 5 9 | 7 6 1 | 4 2 3 |
| 4 2 6 | 8 5 3 | 7 9 1 |
| 7 1 3 | 9 2 4 | 8 5 6 |
+-------+-------+-------+
| 9 6 1 | 5 3 7 | 2 8 4 |
| 2 8 7 | 4 1 9 | 6 3 5 |
| 3 4 5 | 2 8 6 | 1 7 9 |
+-------+-------+-------+
Solution found in 126 milliseconds
```

Using the Console for this application is perfectly fine, defining a `prettyPrint` function to display nicely a problem and its solution should be considered.
