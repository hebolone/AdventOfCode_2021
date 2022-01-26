package Days

import Board
import java.lang.Math.abs

class Day05 : AlgosBase()  {
    override fun Basic(input : MutableList<String>) : Int {
        val parsedDatas = ParseInput(input)
        val board = CreateBoard(parsedDatas)
        parsedDatas.forEach { DrawLine(board, it.from, it.to) }
        //println(board.PrintBoard({ if(it == 0) "." else it.toString() }))

        return FindOverlappedLines(board)
    }

    override fun Advanced(input : MutableList<String>) : Int {
        val parsedDatas = ParseInput(input)
        val board = CreateBoard(parsedDatas)
        parsedDatas.forEach { DrawLine(board, it.from, it.to, true) }
        //println(board.PrintBoard({ if(it == 0) "." else it.toString() }))

        return FindOverlappedLines(board)
    }

    //region Private
    private data class Point(val x : Int, val y : Int)
    private data class Line(val from : Point, val to : Point)

    private fun ParseInput(value: MutableList<String>) : MutableList<Line> {
        //  Consider only horizontal or vertical lines
        val retValue = mutableListOf<Line>()
        val regex = "(?<x1>\\d{1,5}),(?<y1>\\d{1,5}) -> (?<x2>\\d{1,5}),(?<y2>\\d{1,5})".toRegex()
        value.forEach {
            val match = regex.find(it)!!
            val (x1, y1, x2, y2) = match.destructured
            val from = Point(x1.toInt(), y1.toInt())
            val to = Point(x2.toInt(), y2.toInt())
            retValue.add(Line(from, to))
        }
        return retValue
    }

    private fun CreateBoard(lines : MutableList<Line>) : Board<Int> {
        //  Search for max x and max y to create correct size board
        val maxX = lines.maxOf { maxOf(it.from.x, it.to.x) + 1 }
        val maxY = lines.maxOf { maxOf(it.from.y, it.to.y) + 1 }
        return Board(maxX, maxY) { 0 }
    }

    private fun DrawLine(board : Board<Int>, from : Point, to : Point, includeDiagonal : Boolean = false) {
        //  Is line horizontal or vertical ?
        val direction = GuessDirection(from, to)
        when(direction) {
            TDirection.HORIZONTAL -> {
                var actualX = minOf(from.x, to.x)
                val finalX = maxOf(from.x, to.x)
                while(actualX <= finalX) {
                    val actualValue = board[actualX, from.y]
                    board[actualX, from.y] = actualValue + 1
                    actualX ++
                }
            }
            TDirection.VERTICAL -> {
                var actualY = minOf(from.y, to.y)
                val finalY = maxOf(from.y, to.y)
                while(actualY <= finalY) {
                    val actualValue = board[from.x, actualY]
                    board[from.x, actualY] = actualValue + 1
                    actualY ++
                }
            }
            TDirection.DIAGONAL -> {
                if(includeDiagonal) {
                    var actualX = from.x
                    var actualY = from.y
                    val incX = if(from.x - to.x > 0) -1 else 1
                    val incY = if(from.y - to.y > 0) -1 else 1
                    (0..(abs(from.x - to.x))).forEach {
                        val actualValue = board[actualX, actualY]
                        board[actualX, actualY] = actualValue + 1
                        actualX += incX
                        actualY += incY
                    }
                }
            }
        }
    }

    private enum class TDirection { HORIZONTAL, VERTICAL, DIAGONAL, ELSE }

    private fun GuessDirection(from : Point, to : Point) = when {
            from.x == to.x -> TDirection.VERTICAL
            from.y == to.y -> TDirection.HORIZONTAL
            abs(from.x - to.x) == abs(from.y - to.y) -> TDirection.DIAGONAL
            else -> TDirection.ELSE
        }

    private fun FindOverlappedLines(board : Board<Int>) : Int {
        var retValue = 0
        (0 until (board.GetSize())).forEach { if(board.GetLinear(it) > 1) retValue ++ }
        return retValue
    }
    //endregion
}