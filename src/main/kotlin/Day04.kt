import kotlin.properties.Delegates

class Day04 : AlgosBase() {
    override fun Basic(input: MutableList<String>): Int {
        ParseInput(input)
        return NumberExtraction(TTypeOfSearch.FIRSTWINNER)
    }

    override fun Advanced(input: MutableList<String>): Int {
        ParseInput(input)
        return NumberExtraction(TTypeOfSearch.LASTWINNER)
    }

    //region Private
    private fun ParseInput(input : MutableList<String>) {
        //  First value is random numbers
        _RandomNumbers = input[0].split(",").map { it.toInt() }

        //  Second value is list of bingo boards
        var board = BingoBoard()
        var counter = 1
        input.drop(1).forEach { it ->
            if (!it.isEmpty()) {
                val boardRawDatas = it.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
                when (counter) {
                    1 -> {
                        board = BingoBoard()
                        _Boards.add(board)
                    }
                    5 -> counter = 0
                }
                board.AddDatas(boardRawDatas)
                counter++
            }
        }
    }

    private fun NumberExtraction(typeOfSearch : TTypeOfSearch) : Int {
        //Extract all the numbers and stop when there's a winner
        val retValue = -1
        val _winningBoards = mutableListOf<BingoBoard>()
        _RandomNumbers.forEach {
            randomNumber ->
            run {
                _Boards.forEach { board ->
                    board.InsertNumber(randomNumber)
                    if(board.CheckForWin()) {
                        val unmarkedNumbers = board.GetSumOfUnmarkedNumbers()
                        if(typeOfSearch == TTypeOfSearch.FIRSTWINNER)
                            return randomNumber * unmarkedNumbers
                        else {
                            if(!_winningBoards.contains(board)) {
                                _winningBoards.add(board)
                                if(_winningBoards.count() == _Boards.count()) {
                                    return randomNumber * unmarkedNumbers
                                }
                            }
                        }
                    }
                }
            }
        }
        return retValue
    }

    private var _RandomNumbers : List<Int> by Delegates.notNull()
    private val _Boards = mutableListOf<BingoBoard>()
    private enum class TTypeOfSearch { FIRSTWINNER, LASTWINNER }
    private data class Number(val number : Int, var signed : Boolean = false)

    private class BingoBoard {
        private val _Numbers = mutableListOf<Number>()
        private val _WinningPatterns = listOf<List<Int>>(
            //Rows
            listOf(0, 1, 2, 3, 4),
            listOf(5, 6, 7, 8, 9),
            listOf(10, 11, 12, 13, 14),
            listOf(15, 16, 17, 18, 19),
            listOf(20, 21, 22, 23, 24),
            //Columns
            listOf(0, 5, 10, 15, 20),
            listOf(1, 6, 11, 16, 21),
            listOf(2, 7, 12, 17, 22),
            listOf(3, 8, 13, 18, 23),
            listOf(4, 9, 14, 19, 24),
            )

        fun AddDatas(numbers : List<Int>) = numbers.forEach { _Numbers.add(Number(it)) }

        fun InsertNumber(number : Int) {
            _Numbers.firstOrNull() { it.number == number }?.signed = true
        }

        fun CheckForWin() : Boolean {
            _WinningPatterns.forEach {
                winningPattern ->
                run {
                    var patternFound = true
                    winningPattern.forEach { if(!_Numbers[it].signed) patternFound = false  }
                    if(patternFound)
                        return patternFound
                }
            }
            return false
        }

        fun GetSumOfUnmarkedNumbers() : Int = _Numbers.filter{ it.signed == false }.sumOf { it.number }
    }
    //endregion
}