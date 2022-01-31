package Days

import BoardExtended
import Coordinate

class Day15 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        ParseInput(input)
        _EndingTile = _Board?.GetEnding()!!
        return _Board!!.AStarAlgorithm(_StartingTile, _EndingTile)
    }

    override fun Advanced(input : MutableList<String>) : Int {
        if(_Board == null) ParseInput(input)
        _Board = _Board?.Multiply(5)
        _EndingTile = _Board?.GetEnding()!!
        return _Board!!.AStarAlgorithm(_StartingTile, _EndingTile)
    }

    //region Private
    private var _StartingTile = Coordinate(0, 0)
    private var _EndingTile = Coordinate(0, 0)
    private var _Board : BoardExtended<Int>? = null

    private data class AStarNode(val coordinate: Coordinate, var f : Int = 0, var g : Int = 0, var h : Int = 0, var parent : AStarNode? = null)

    private fun ParseInput(lines : MutableList<String>) {
        val height = lines.count()
        val width = lines.first().length
        _Board = BoardExtended(width, height) { 0 }
        var lineCounter = 0
        lines.forEach { y -> run {
                y.forEachIndexed { index, value -> run { _Board!![index, lineCounter] = value.digitToInt() } }
                lineCounter ++
            }
        }
    }

    private fun BoardExtended<Int>.AStarAlgorithm(starting : Coordinate, ending : Coordinate) : Int {
        val openSet = mutableSetOf<AStarNode>()
        val closedSet = mutableSetOf<AStarNode>()
        val starting_Node = AStarNode(starting)
        val ending_Node = AStarNode(ending)
        openSet.add(starting_Node)

        while(openSet.isNotEmpty()) {
            var currentNode = openSet.minByOrNull { it.f }!!

            if(currentNode.coordinate == ending_Node.coordinate) {
                return RebuildPath(currentNode, false)
            }

            openSet.removeIf { it.coordinate == currentNode.coordinate }
            closedSet.add(currentNode)

            //  Get all children
            listOf(
                Coordinate(currentNode.coordinate.x - 1, currentNode.coordinate.y),
                Coordinate(currentNode.coordinate.x, currentNode.coordinate.y - 1),
                Coordinate(currentNode.coordinate.x + 1, currentNode.coordinate.y),
                Coordinate(currentNode.coordinate.x, currentNode.coordinate.y + 1)
            ).filter {
                //  Exclude cells outside board
                GetOrNull(it.x, it.y) != null
            }.filter {
                //  Exclude tile I'm coming from (makes no sense!)
                currentNode.coordinate != it
            }.forEach lit@{
                //  Get only real children
                //  Search this child inside my closed set
                var childFoundInClosedSet = closedSet.firstOrNull { _n -> _n.coordinate == it }
                if(childFoundInClosedSet != null)
                    return@lit

                var child = AStarNode(it, parent = currentNode)
                child.g = currentNode.g + this[child.coordinate]
                child.h = 0
                child.f = child.g + child.h

                openSet.filter { _n -> _n.coordinate == it }.forEach { _c ->
                    if(child.g > _c.g)
                        return@lit
                }

                openSet.add(child)
            }
        }

        return -1
    }

    private fun RebuildPath(ending: AStarNode, print: Boolean = false) : Int {
        var currentNode : AStarNode? = ending
        val path = mutableListOf<AStarNode>()
        while(currentNode != null) {
            path.add(currentNode)
            currentNode = currentNode.parent
        }
        path.reverse()
        if(print) {
            path.forEach {
                print("(${it.coordinate.x}-${it.coordinate.y}):${it.f},")
            }
            println("Cost: ${path.sumOf { _Board!![it.coordinate.x, it.coordinate.y] } - _Board!![0]}, ")
        }
        return path.sumOf { _Board!![it.coordinate.x, it.coordinate.y] } - _Board!![0]
    }

    private fun BoardExtended<Int>.Multiply(value : Int) : BoardExtended<Int> {
        var retValue = BoardExtended<Int>(this.x * value, this.y * value) { 0 }
        (0 until GetSize()).forEach { o ->
            val coordinate = this.GetCoordinateFromIndex(o)
            (0 until value).forEach { n ->
                retValue[coordinate.x + (n * x), coordinate.y] = when {
                    this[coordinate] + n > 9 -> this[coordinate] + n - 9
                    else -> this[coordinate] + n
                }
            }
        }
        (0 until (x * value)).forEach { _x ->
            (0 until y).forEach { _y ->
                (0 until value).forEach { n ->
                    retValue[_x, _y + (n * y)] = when {
                        retValue[_x, _y] + n > 9 -> retValue[_x, _y] + n - 9
                        else -> retValue[_x, _y] + n
                    }
                }
            }
        }
        return retValue
    }

    private fun BoardExtended<Int>.GetEnding() : Coordinate = Coordinate(x - 1, y - 1)
    //endregion
}