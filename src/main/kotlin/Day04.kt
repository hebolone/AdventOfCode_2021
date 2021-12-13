import kotlin.properties.Delegates

class Day04 : AlgosBase()  {
    override fun Basic(input : MutableList<String>) : Int {
        var datas = if(IsTest) _InputTest.split(regex = "\\n".toRegex()).toMutableList() else input
        ParseInput(datas)
        val result = NumberExtraction(TTypeOfSearch.FIRSTWINNER)

        return result
    }

    override fun Advanced(input : MutableList<String>) : Int {
        var datas = if(IsTest) _InputTest.split(regex = "\\n".toRegex()).toMutableList() else input
        ParseInput(datas)
        val result = NumberExtraction(TTypeOfSearch.LASTWINNER)

        return result
    }

    //region Private
    private fun ParseInput(input : MutableList<String>) {
        //  First value is random numbers
        _RandomNumbers = input[0].split(",").map { it.toInt() }

        //  Second value is list of bingo boards
        var board = Board()
        var counter = 1
        input.drop(1).forEach {
            if (!it.isNullOrEmpty()) {
                val boardRawDatas = it.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
                when (counter) {
                    1 -> {
                        board = Board()
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
        var retValue = -1
        var _winningBoards = mutableListOf<Board>()
        _RandomNumbers.forEach {
            randomNumber ->
            run {
                _Boards.forEach { board ->
                    board.InsertNumber(randomNumber)
                    if(board.CheckForWin()) {
                        var unmarkedNumbers = board.GetUnmarkedNumbers()
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
    private val _Boards = mutableListOf<Board>()
    private enum class TTypeOfSearch { FIRSTWINNER, LASTWINNER }
    private val _InputTest = """7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
"""

    private data class Number(val number : Int, var signed : Boolean = false)

    private class Board {
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

        fun GetUnmarkedNumbers() : Int = _Numbers.filter{ it.signed == false }.sumOf { it.number }
    }
    //endregion
}