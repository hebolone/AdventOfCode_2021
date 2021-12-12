class Algos {
    private val _Algos : MutableMap<String, (MutableList<String>) -> Int> = mutableMapOf()

    init {
        val day01 = Day01()
        val day02 = Day02()
        val day03 = Day03()
        _Algos["01a"] = day01::Basic
        _Algos["01b"] = day01::Advanced
        _Algos["02a"] = day02::Basic
        _Algos["02b"] = day02::Advanced
        _Algos["03a"] = day03::Basic
        _Algos["03b"] = day03::Advanced
    }

    enum class TYPEOFQUESTION(val symbol : String) { BASIC("a"), ADVANCED("b") }

    fun GetAlgo(day : Int, typeOfQuestion : TYPEOFQUESTION) : ((MutableList<String>) -> Int)? = _Algos["${Utils.GetCorrectNumber(day)}${typeOfQuestion.symbol}"]
}

