package connectfour

const val DEF6 = 6
const val DEF7 = 7
const val R5 = 5
const val R9 = 9
const val WIN_COUNT = 4

class Game(private val firstPlayerName: String, private val secondPlayerName: String) {
    private var board = Board(DEF6, DEF7)
    private var lastGameFirst = ""
    private var lastTurnPlayer = ""
    private var isWinner = false
    private var gamesCount = 1
    private val score = mutableMapOf<String, Int>(firstPlayerName to 0, secondPlayerName to 0)
    private var stopGame = false

    fun startNewGame() {
        createBoard()
        selectGamesCount()
        gameInfo()
        if (gamesCount > 1) {
            for (i in 1..gamesCount) {
                println("Game #$i")
                startGame()
                if (stopGame) break
            }
        } else {
            startGame()
        }
        println("Game over!")
    }

    private fun createBoard() {
        println(
            "Set the board dimensions (Rows x Columns)\n" + "Press Enter for default (6 x 7)"
        )

        val boardInput = readLine()!!.lowercase().trim()
        if (boardInput.matches(Regex("\\d+\\h*x\\h*\\d+"))) {
            val size = boardInput.split("x").map { it.trim().toInt() }.toList()
            val rowsInRange = size.first() in R5..R9
            val colsInRange = size.last() in R5..R9
            if (rowsInRange && colsInRange) {
                board = Board(size.first(), size.last())
            } else {
                if (!rowsInRange) println("Board rows should be from 5 to 9")
                if (!colsInRange) println("Board columns should be from 5 to 9")
                createBoard()
            }
        } else {
            if (boardInput.isNotEmpty()) {
                println("Invalid input")
                createBoard()
            }
        }
    }

    private fun gameInfo() {
        println(
            "$firstPlayerName VS $secondPlayerName\n" + "${board.rows} X ${board.cols} board"
        )
        println(if (gamesCount == 1) "Single game" else "Total $gamesCount games")
    }

    private fun startGame() {
        board.draw()
        val colOverflowList = mutableListOf<Int>()
        repeat(board.cols) {
            colOverflowList.add(board.rows)
        }
        val totalCells = board.rows * board.cols
        var filledCells = 0
        lastTurnPlayer =
            if (lastGameFirst.isEmpty() || lastGameFirst == secondPlayerName) secondPlayerName else firstPlayerName
        lastGameFirst = if (lastTurnPlayer == secondPlayerName) firstPlayerName else secondPlayerName
        while (!isWinner && filledCells != totalCells) {
            val currentPlayerName = if (lastTurnPlayer == firstPlayerName) secondPlayerName else firstPlayerName
            println("$currentPlayerName's turn:")
            val col = readLine()!!

            if (col.toIntOrNull() != null && col.isNotEmpty() && col.toInt() in 1..board.cols) {
                val intCol = col.toInt()
                colOverflowList[intCol - 1]--
                if (colOverflowList[intCol - 1] < 0) {
                    println("Column $intCol is full")
                } else {
                    lastTurnPlayer = makeTurn(intCol)
                    filledCells++
                }
            } else if (col == "end") {
                stopGame = true
                break
            } else if (col.toIntOrNull() == null) {
                println("Incorrect column number")
            } else {
                println("The column number is out of range (1 - ${board.cols})")
            }
        }
        if (isWinner) {
            println("Player $lastTurnPlayer won")
            score[lastTurnPlayer] = score[lastTurnPlayer]!! + 2
        } else if (filledCells == totalCells) {
            println("It is a draw")
            for (playerScore in score) {
                score[playerScore.key] = playerScore.value + 1
            }
        }
        if (gamesCount > 1) {
            println("Score")
            println("$firstPlayerName: ${score[firstPlayerName]} $secondPlayerName: ${score[secondPlayerName]}")
        }
        board.reset()
        resetGame()
    }

    private fun makeTurn(intCol: Int): String {
        return if (lastTurnPlayer.isEmpty() || lastTurnPlayer == secondPlayerName) {
            board.playerTurn("o", intCol)
            isWinner = board.checkWin()
            firstPlayerName
        } else {
            board.playerTurn("*", intCol)
            isWinner = board.checkWin()
            secondPlayerName
        }
    }

    private fun selectGamesCount() {
        println(
            "Do you want to play single or multiple games?\n" +
                "For a single game, input 1 or press Enter\n" +
                "Input a number of games:"
        )
        try {
            val input = readLine()!!
            if (input.isEmpty()) {
                gamesCount = 1
            } else if (input.toInt() <= 0) {
                println("Invalid input")
                selectGamesCount()
            } else {
                gamesCount = input.toInt()
            }
        } catch (e: NumberFormatException) {
            println("Invalid input")
            selectGamesCount()
        }
    }

    private fun resetGame() {
        lastTurnPlayer = ""
        isWinner = false
    }
}
