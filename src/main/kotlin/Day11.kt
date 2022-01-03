class Day11 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        val datas = if(IsTest) LoadTestDatas("/home/simone/Scrivania/AdventOfCode/day_11_test.txt") else input
        val board = ParseInput(datas)
        var totalFlashes = 0
        (1..100).forEach { totalFlashes += board.NextCycle() }
        return totalFlashes
    }

    override fun Advanced(input : MutableList<String>) : Int {
        val datas = if(IsTest) LoadTestDatas("/home/simone/Scrivania/AdventOfCode/day_11_test.txt") else input
        val board = ParseInput(datas)
        var stepSynchronized = 0
        var found = false
        do {
            val flashes = board.NextCycle()
            if(flashes == board.GetSize()) {
                found = true
            }
            stepSynchronized ++
        } while(!found)
        return stepSynchronized
    }

    //region Private
    private data class Octopus(var Energy : Int, var Flashes :  Int = 0, var GoingToFlash : Boolean = false)
    private data class Coordinates(val x : Int, val y : Int)

    private fun ParseInput(lines : MutableList<String>) : BoardExtended<Octopus> {
        val columns = lines.first().length
        val rows = lines.count()
        val board = BoardExtended(columns, rows) { Octopus(0) }

        //  Fill board
        lines.forEachIndexed { index, it ->
            run {
                var line = 0
                for (c in it) {
                    board[line, index].Energy = c.digitToInt()
                    line ++
                }
            }
        }

        return board
    }

    private fun BoardExtended<Octopus>.NextCycle() : Int {
        var totalFlashes = 0
        //  Increment energy
        (0 until GetSize()).forEach {
            this[it].Energy++
            if(this[it].Energy > 9) {
                this[it].Energy = 0
                this[it].GoingToFlash = true
            }
        }
        while(AreThereFlashes()) {
            (0 until GetSize()).forEach {
                if(this[it].GoingToFlash) {
                    Flash(it)
                    totalFlashes ++
                }
            }
        }

        return totalFlashes
    }

    private fun BoardExtended<Octopus>.Flash(x : Int, y : Int) {
        listOf(
            Coordinates(x - 1, y - 1),
            Coordinates(x, y - 1),
            Coordinates(x + 1, y - 1),
            Coordinates(x - 1, y),
            Coordinates(x + 1, y),
            Coordinates(x - 1, y + 1),
            Coordinates(x , y + 1),
            Coordinates(x + 1, y + 1),
        ).forEach {
            val octopus = GetOrNull(it.x, it.y)
            if(octopus != null && octopus.Energy > 0) {
                octopus.Energy++
                if(octopus.Energy > 9) {
                    octopus.Energy = 0
                    octopus.GoingToFlash = true
                }
            }
        }
        this[x, y].GoingToFlash = false
    }

    private fun BoardExtended<Octopus>.Flash(index : Int) {
        val coordinates = GetCoordinatesFromIndex(index)
        Flash(coordinates.first, coordinates.second)
    }

    private fun BoardExtended<Octopus>.AreThereFlashes() : Boolean {
        var retValue = false
        (0 until GetSize()).forEach {
            if(this[it].GoingToFlash)
                retValue = true
        }
        return retValue
    }
    //endregion
}