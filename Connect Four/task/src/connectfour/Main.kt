package connectfour

fun main() {
    println("Connect Four")

    println("First player's name:")
    val firstPlayerName = readLine()!!

    println("Second player's name:")
    val secondPlayerName = readLine()!!

    val newGame = Game(firstPlayerName, secondPlayerName)
    newGame.startNewGame()
}
