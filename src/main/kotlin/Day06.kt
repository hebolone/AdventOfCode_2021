import kotlin.properties.Delegates

class Day06 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : ULong {
        val datas = input.first().split(",").toMutableList()
        (0.._MaxValue).forEach { _Map[it] = 0u }
        MapStart(datas.map { it.toInt() })
        (1..80).forEach { _Map.NextCycle() }
        return _Map.ValuesCount()
    }

    override fun Advanced(input : MutableList<String>) : ULong {
        val datas = input.first().split(",").toMutableList()
        (0.._MaxValue).forEach { _Map[it] = 0u }
        MapStart(datas.map { it.toInt() })
        (1..256).forEach { _Map.NextCycle() }
        return _Map.ValuesCount()
    }

    //region Private
    private val _InputTest = "3,4,3,1,2"
    private val _MaxValue = 8
    private val _Map : MutableMap<Int, ULong> = mutableMapOf()

    private fun MapStart(values : List<Int>) = values.forEach { _Map[it] = _Map[it]!! + 1u }

    private fun MutableMap<Int, ULong>.NextCycle() {
        var fish6 : ULong = 0u
        var fish8 : ULong = 0u
        (0 until this.size).forEach {
            var buffer = _Map[it]!!

            when(it) {
                0 -> {
                    val fish0 = _Map[0]!!
                    fish6 += fish0
                    fish8 += fish0
                }
                else -> _Map[it - 1] = buffer
            }
        }
        _Map[6] = _Map[6]!! + fish6
        _Map[_MaxValue] = fish8
    }

    private fun MutableMap<Int, ULong>.ValuesCount() : ULong = this.values.sumOf { it }
    //endregion
}