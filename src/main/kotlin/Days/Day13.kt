package Days

import BoardExtended

class Day13 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        ParseInput(input)
        ExecuteCommands(TCOMMANDEXECUTION.FIRST)
        return _Board.CountDots()
    }

    override fun Advanced(input : MutableList<String>) : Int {
        if(_Board.GetSize() == 0) ParseInput(input)
        ExecuteCommands(TCOMMANDEXECUTION.ALL)
        //  Get last size
        val lastX = _Commands.last { it.order == TORDER.X }.value
        val lastY = _Commands.last { it.order == TORDER.Y }.value
        val smallBoard = _Board.Slice(lastX, lastY)
        println(smallBoard.PrintBoard { if (it == 1) "*" else " " })
        return -1
    }

    //region Private
    private var _Board : BoardExtended<Int> = BoardExtended(0, 0) { 0 }
    private val _Dots = mutableListOf<Dot>()
    private val _Commands = mutableListOf<Command>()
    private enum class TORDER { X, Y }
    private enum class TCOMMANDEXECUTION { FIRST, ALL }
    private data class Dot(val x : Int, val y : Int)
    private data class Command(val order : TORDER, val value : Int)

    private fun ParseInput(input : MutableList<String>) {
        //  Gets all dots
        input.forEach {
            ParseSingleLine(it)
        }

        //  Get max x and max y
        val x = _Dots.maxOf { it.x }
        val y = _Dots.maxOf { it.y }

        //  Fill with dots
        _Board = BoardExtended(x + 1, y + 1) { 0 }
        _Dots.forEach {
            _Board[it.x, it.y] = 1
        }
    }

    private fun ExecuteCommands(commandExecution : TCOMMANDEXECUTION) {
        var commandsToExecute = when(commandExecution) {
            TCOMMANDEXECUTION.FIRST -> mutableListOf(_Commands.first())
            TCOMMANDEXECUTION.ALL -> _Commands
        }
        commandsToExecute.forEach {
            when(it.order) {
                TORDER.X -> {
                    //  Folding vertically
                    //  Parse all board
                    ((it.value + 1) until _Board.x).forEach { x ->
                        run {
                            (0 until _Board.y).forEach { y ->
                                run {
                                    if (_Board[x, y] == 1) {
                                        val symX = (it.value * 2) - x
                                        _Board[symX, y] = 1
                                        _Board[x, y] = 0
                                    }
                                }
                            }
                        }
                    }
                }
                TORDER.Y -> {
                    //  Folding horizontally
                    ((it.value + 1) until _Board.y).forEach { y ->
                        run {
                            (0 until _Board.x).forEach { x ->
                                run {
                                    if (_Board[x, y] == 1) {
                                        val symY = (it.value * 2) - y
                                        _Board[x, symY] = 1
                                        _Board[x, y] = 0
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun ParseSingleLine(line : String) {
        if(line.isNotEmpty())
        when(line.first()) {
            in '0'..'9' -> {
                //  This is a dot
                val regex = "(\\d+),(\\d+)".toRegex()
                val matchResult = regex.find(line)!!
                val (x, y) = matchResult.destructured
                _Dots.add(Dot(x.toInt(), y.toInt()))
            }
            'f' -> {
                //  this is a command
                val regex = "([xy]+)=(\\d+)".toRegex()
                val matchResult = regex.find(line)!!
                val (order, value) = matchResult.destructured
                _Commands.add(Command(TORDER.valueOf(order.uppercase()), value.toInt()))
            }
        }
    }

    private fun BoardExtended<Int>.CountDots() : Int {
        var retValue = 0
        (0 until this.GetSize()).forEach { retValue += this[it] }
        return retValue
    }
    //endregion
}