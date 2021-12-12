class Algos {
    private val _Algos : MutableMap<String, (MutableList<String>) -> Int> = mutableMapOf()

    init {
        _Algos["01a"] = ::Algo_01a
        _Algos["01b"] = ::Algo_01b
        _Algos["02a"] = ::Algo_02a
    }

    enum class TYPEOFQUESTION(val symbol : String) { BASIC("a"), ADVANCED("b") }

    fun GetAlgo(day : Int, typeOfQuestion : TYPEOFQUESTION) : ((MutableList<String>) -> Int)? = _Algos["${Utils.GetCorrectNumber(day)}${typeOfQuestion.symbol}"]

    fun Algo_01a(input : MutableList<String>) : Int {
        var retValue = 0
        var buffer = input.first().toInt()
        input.drop(1).map{ it.toInt() }.forEach {
            if(it > buffer) retValue ++
            buffer = it
        }
        return retValue
    }

    fun Algo_01b(input : MutableList<String>) : Int {
        val buffers = IntArray(2000)
        input.map{ it.toInt() }.forEachIndexed { index, i ->
            buffers[index] = i
            if(index > 0) buffers[index - 1] += i
            if(index > 1) buffers[index - 2] += i
        }

        //  Applying first algo
        return Algo_01a(buffers.map{ it.toString() }.toMutableList())
    }

    fun Algo_02a(input : MutableList<String>) : Int {
        var retValue = 0

        return retValue
    }
}