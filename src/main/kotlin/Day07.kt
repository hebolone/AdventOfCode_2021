class Day07 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        val datas = input.first().split(",").map { it.toInt() }
        val minPosition = datas.minOf { it }
        val maxPosition = datas.maxOf { it }
        val fuelNeeded = mutableListOf<Int>()
        (minPosition..maxPosition).forEach { fuelNeeded.add(CalculateFuel(datas, it)) }
        return fuelNeeded.minOf { it }
    }

    override fun Advanced(input : MutableList<String>) : Int {
        val datas = input.first().split(",").map { it.toInt() }
        val minPosition = datas.minOf { it }
        val maxPosition = datas.maxOf { it }
        val fuelNeeded = mutableListOf<Int>()
        (minPosition..maxPosition).forEach { fuelNeeded.add(CalculateFuel(datas, it, ::Summation)) }
        return fuelNeeded.minOf { it }
    }

    //region Private
    private val _Cache = mutableMapOf<Int, Int>()

    private fun CalculateFuel(crabs : List<Int>, position : Int, summation : (value : Int) -> Int = { it }) : Int {
        var totalFuel = 0
        crabs.forEach { totalFuel += summation(kotlin.math.abs(it - position)) }
        return totalFuel
    }

    private fun Summation(value : Int) : Int {
        var retValue : Int
        if(_Cache.containsKey(value))
            retValue = _Cache[value]!!
        else {
            retValue = (value * (value + 1)) / 2
            _Cache[value] = retValue
        }
        return retValue

    }
    //endregion
}