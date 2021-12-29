class Day09 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        val datas = if(IsTest) LoadTestDatas("/home/simone/Scrivania/AdventOfCode/day_09_test.txt") else input
        val board = ParseInput(datas)
        val lowestPoints = FindLowestPoints(board)
        return lowestPoints.sumOf { it.value + 1 }
    }

    override fun Advanced(input : MutableList<String>) : Int {
        val datas = if(IsTest) LoadTestDatas("/home/simone/Scrivania/AdventOfCode/day_09_test.txt") else input
        val board = ParseInput(datas)
        val basins = SearchForBasins(board)
        basins.sortByDescending { it.tiles.count() }
        var retValue = 1
        basins.take(3).forEach { retValue *= it.tiles.count() }
        return retValue
    }

    //region Private
    private fun ParseInput(lines : MutableList<String>) : BoardExtended<Tile> {
        //  get length and height of the board
        val columns = lines.first().length
        val rows = lines.count()
        val board = BoardExtended(columns, rows) { Tile(0, 0, 0) }

        //  Fill board
        lines.forEachIndexed { index, it ->
            run {
                var line = 0
                for (c in it) {
                    val tile = board.Get(line, index)
                    tile.x = line
                    tile.y = index
                    tile.value = c.digitToInt()
                    line++
                }
            }
        }

        return board
    }

    private fun FindLowestPoints(board : BoardExtended<Tile>) : MutableList<Tile> {
        val lowestPoints = mutableListOf<Tile>()
        (0 until board.x).forEach { x ->
            run {
                (0 until board.y).forEach { y ->
                    val i = IsLowestPoint(board, x, y)
                    if(i)
                        lowestPoints.add(board.Get(x, y))
                }
            }
        }

        return lowestPoints
    }

    private fun SearchForBasins(board : BoardExtended<Tile>) : MutableList<Basin> {
        val retValue = mutableListOf<Basin>()

        //  Let's start from lowest points
        FindLowestPoints(board).forEach {
            val basin = Basin()
            retValue.add(basin)
            FillBasin(board, it.x, it.y, basin)
        }

        return retValue
    }

    private fun IsLowestPoint(board : BoardExtended<Tile>, x : Int, y: Int) : Boolean {
        val top = board.GetOrNull(x, y - 1)?.value ?: 10
        val bottom = board.GetOrNull(x, y + 1)?.value ?: 10
        val left = board.GetOrNull(x - 1, y)?.value ?: 10
        val right = board.GetOrNull(x + 1, y)?.value ?: 10
        val currentValue = board.Get(x, y).value
        return currentValue < top && currentValue < bottom && currentValue < left && currentValue < right
    }

    private fun FillBasin(board : BoardExtended<Tile>, x : Int, y: Int, basin : Basin) {
        val currentTile = board.GetOrNull(x, y) ?: return
        if(!basin.tiles.contains(currentTile))
            basin.tiles.add(currentTile)

        data class Coordinate(val x : Int, val y : Int)

        listOf(
            Coordinate(x, y - 1),
            Coordinate(x + 1, y),
            Coordinate(x, y + 1),
            Coordinate(x - 1, y)
        ).forEach {
            val tile = board.GetOrNull(it.x, it.y) ?: return
            if(tile?.value > currentTile.value && tile.value < 9) {
                FillBasin(board, it.x, it.y, basin)
            }
        }
    }

    private data class Tile(var x : Int, var y : Int, var value : Int)

    private data class Basin(val tiles : MutableList<Tile> = mutableListOf())
    //endregion
}