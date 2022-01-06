import java.lang.Exception

class Day12 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        ParseInput(input)
        _PathsToTheEnd = 0
        _MaxSmallCaveVisits = 1
        GetAllPaths()
        return _PathsToTheEnd
    }

    override fun Advanced(input : MutableList<String>) : Int {
        if(_Caves.isEmpty()) ParseInput(input)
        _PathsToTheEnd = 0
        _MaxSmallCaveVisits = 2
        GetAllPaths()
        return _PathsToTheEnd
    }

    //region Private
    private enum class  TCAVETYPE { BIGCAVE, SMALLCAVE, START, END }
    private data class Cave(val name : String, val caveType : TCAVETYPE, val connected : MutableList<Cave>)
    private val _Caves = mutableListOf<Cave>()
    private var _PathsToTheEnd = 0
    private var _MaxSmallCaveVisits = 0

    private fun ParseInput(input : MutableList<String>) {
        input.forEach {
            val nodes = it.split("-")
            //  Sort alphabetically
            _Caves.AddCave(nodes[0], nodes[1])
            _Caves.AddCave(nodes[1], nodes[0])
        }
    }

    private fun MutableList<Cave>.AddCave(from : String, to : String) {
        if(!this.any { it.name == from })
            this.add(CreateCave(from))
        val nodeFrom = this.first { it.name == from }

        if(!this.any { it.name == to })
            this.add(CreateCave(to))

        val nodeTo = this.first { it.name == to }

        nodeFrom.connected.add(nodeTo)
    }

    private fun CreateCave(name : String) : Cave {
        val caveType : TCAVETYPE = when {
            name == "start" -> TCAVETYPE.START
            name == "end" -> TCAVETYPE.END
            name.IsUpperCase() -> TCAVETYPE.BIGCAVE
            name.IsLowerCase() -> TCAVETYPE.SMALLCAVE
            else -> throw Exception("Cannot determine cave type: $name")
        }
        return Cave(name, caveType, mutableListOf())
    }

    private fun GetAllPaths() {
        //  Get starting point
        val startingCave = _Caves.first {it.caveType == TCAVETYPE.START }
        var currentPath = ""
        var visitedCaves = mutableListOf<Cave>()
        Walk(startingCave, currentPath, visitedCaves)
    }

    private fun Walk(currentCave : Cave, pathString : String, visitedCaves : MutableList<Cave>) {
        val separator = if(currentCave.caveType == TCAVETYPE.START) "" else ","
        val localCurrentPath = "$pathString$separator${currentCave.name}"
        val localVisitedCaves = visitedCaves.toMutableList()

        when(currentCave.caveType) {
            TCAVETYPE.END -> {
                _PathsToTheEnd ++
                return
            }
            TCAVETYPE.START,
            TCAVETYPE.SMALLCAVE -> {
                localVisitedCaves.add(currentCave)
            }
        }

        //  Check max value of visits
        currentCave.connected.filter { c -> localVisitedCaves.CountPrevious(c) < (if(localVisitedCaves.SmallCavesFulfilled()) 1 else _MaxSmallCaveVisits) }.forEach {
            Walk(it, localCurrentPath, localVisitedCaves)
        }
    }

    private fun String.IsUpperCase() : Boolean = this[0] in 'A'..'Z'
    private fun String.IsLowerCase() : Boolean = this[0] in 'a'..'z'
    private fun MutableList<Cave>.CountPrevious(cave : Cave) : Int = when(cave.caveType) {
        TCAVETYPE.START -> 2
        TCAVETYPE.END -> 0
        TCAVETYPE.SMALLCAVE -> this.count { it === cave }
        TCAVETYPE.BIGCAVE -> 0
    }
    private fun MutableList<Cave>.SmallCavesFulfilled() : Boolean = this.filter { it.caveType == TCAVETYPE.SMALLCAVE }.groupingBy { it.name }.eachCount().any { it.component2() >= 2 }
    //endregion
}