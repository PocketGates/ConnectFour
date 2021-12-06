package connectfour

class Board(val rows: Int, val cols: Int) {
    private val initBoard = mutableListOf<MutableList<String>>()
    private var lastSign = ""
    private var lastCol = -1

    init {
        for (i in 0 until rows) {
            initBoard.add(mutableListOf())
            for (k in 0 until cols) {
                initBoard[i].add(" ")
            }
        }
    }

    /**
     * Makes player's turn and draws a board
     *
     * @param sign a symbol for player's turn
     * @param col a col for player's turn
     * */
    fun playerTurn(sign: String, col: Int) {
        for (row in initBoard.lastIndex downTo 0) {
            if (initBoard[row][col - 1] == " ") {
                initBoard[row][col - 1] = sign
                break
            }
        }
        lastSign = sign
        lastCol = col
        draw()
    }

    /**
     * Draws a board in console
     * */
    fun draw() {
        for (k in 0..cols) {
            print(if (k <= cols - 1) " ${k + 1}" else "\n")
        }

        for (i in 0 until rows) {
            for (k in 0..cols) {
                print(if (k < cols) "║${initBoard[i][k]}" else "║\n")
            }
        }

        for (k in 0..cols) {
            print(
                when (k) {
                    0 -> {
                        "╚═"
                    }
                    in 1 until cols -> {
                        "╩═"
                    }
                    else -> {
                        "╝\n"
                    }
                }
            )
        }
    }

    fun checkWin(): Boolean {
        val vertical = checkVertical()
        val horizontal = checkHorizontal()
        val diagonal = checkDiagonal()
        return vertical || horizontal || diagonal
    }

    private fun checkVertical(): Boolean {
        var count = 0
        var startRow = 0
        for (row in initBoard.lastIndex downTo 0) {
            if (initBoard[row][lastCol - 1] == lastSign) {
                startRow = row
                break
            }
        }

        for (row in startRow downTo 0) {
            if (initBoard[row][lastCol - 1] == lastSign) {
                count++
            } else if (count == WIN_COUNT) {
                break
            } else count = 0
        }

        return count == WIN_COUNT
    }

    private fun checkHorizontal(): Boolean {
        var count = 0
        var startRow = 0
        for (row in initBoard.lastIndex downTo 0) {
            if (initBoard[row][lastCol - 1] == lastSign) {
                startRow = row
                break
            }
        }

        for (column in initBoard[startRow]) {
            if (column == lastSign) {
                count++
            } else if (count == WIN_COUNT) {
                break
            } else count = 0
        }

        return count == WIN_COUNT
    }

    private fun checkDiagonal(): Boolean {
        val startCol = lastCol - 1
        var leftCount = 0
        var rightCount = 0
        var startRow = 0
        findRow@ for (row in 0..initBoard.lastIndex) {
            if (initBoard[row][startCol] == lastSign) {
                startRow = row
                break@findRow
            }
        }

        var colLeftOffset = startCol
        for (row in startRow..initBoard.lastIndex) {
            if (colLeftOffset >= 0 && initBoard[row][colLeftOffset] == lastSign) {
                leftCount++
                colLeftOffset--
            } else if (leftCount == WIN_COUNT) break
            else leftCount = 0
        }


        var colOffset = startCol
        for (row in startRow..initBoard.lastIndex) {
            if (colOffset <= initBoard.first().lastIndex && initBoard[row][colOffset] == lastSign) {
                rightCount++
                colOffset++
            } else if (rightCount == WIN_COUNT) {
                break
            } else rightCount = 0
        }

        return leftCount == WIN_COUNT || rightCount == WIN_COUNT
    }

    fun reset() {
        lastSign = ""
        lastCol = -1
        initBoard.clear()
        for (i in 0 until rows) {
            initBoard.add(mutableListOf())
            for (k in 0 until cols) {
                initBoard[i].add(" ")
            }
        }
    }
}
