import kotlin.properties.Delegates

class Algos {
    private var _Algos : List<AlgoDefinition> by Delegates.notNull()

    init {
        val day01 = Day01()
        val day02 = Day02()
        val day03 = Day03()
        val day04 = Day04()
        val day05 = Day05()
        val day06 = Day06()
        val day07 = Day07()
        val day08 = Day08()
        val day09 = Day09()
        val day10 = Day10()
        val day11 = Day11()
        val day12 = Day12()

        _Algos = listOf(
            AlgoDefinition(1, TYPEOFQUESTION.BASIC, day01::Basic),
            AlgoDefinition(1, TYPEOFQUESTION.ADVANCED, day01::Advanced),
            AlgoDefinition(2, TYPEOFQUESTION.BASIC, day02::Basic),
            AlgoDefinition(2, TYPEOFQUESTION.ADVANCED, day02::Advanced),
            AlgoDefinition(3, TYPEOFQUESTION.BASIC, day03::Basic),
            AlgoDefinition(3, TYPEOFQUESTION.ADVANCED, day03::Advanced),
            AlgoDefinition(4, TYPEOFQUESTION.BASIC, day04::Basic),
            AlgoDefinition(4, TYPEOFQUESTION.ADVANCED, day04::Advanced),
            AlgoDefinition(5, TYPEOFQUESTION.BASIC, day05::Basic),
            AlgoDefinition(5, TYPEOFQUESTION.ADVANCED, day05::Advanced),
            AlgoDefinition(6, TYPEOFQUESTION.BASIC, day06::Basic),
            AlgoDefinition(6, TYPEOFQUESTION.ADVANCED, day06::Advanced),
            AlgoDefinition(7, TYPEOFQUESTION.BASIC, day07::Basic),
            AlgoDefinition(7, TYPEOFQUESTION.ADVANCED, day07::Advanced),
            AlgoDefinition(8, TYPEOFQUESTION.BASIC, day08::Basic),
            AlgoDefinition(8, TYPEOFQUESTION.ADVANCED, day08::Advanced),
            AlgoDefinition(9, TYPEOFQUESTION.BASIC, day09::Basic),
            AlgoDefinition(9, TYPEOFQUESTION.ADVANCED, day09::Advanced),
            AlgoDefinition(10, TYPEOFQUESTION.BASIC, day10::Basic),
            AlgoDefinition(10, TYPEOFQUESTION.ADVANCED, day10::Advanced),
            AlgoDefinition(11, TYPEOFQUESTION.BASIC, day11::Basic),
            AlgoDefinition(11, TYPEOFQUESTION.ADVANCED, day11::Advanced),
            AlgoDefinition(12, TYPEOFQUESTION.BASIC, day12::Basic),
            AlgoDefinition(12, TYPEOFQUESTION.ADVANCED, day12::Advanced),
        )
    }

    enum class TYPEOFQUESTION(val symbol : String) { BASIC("a"), ADVANCED("b") }

    data class AlgoDefinition(val day : Int, val typeOfQuestion: TYPEOFQUESTION, val algorythm : (MutableList<String>) -> Any)

    fun GetAlgo(day : Int, typeOfQuestion : TYPEOFQUESTION) : ((MutableList<String>) -> Any)? = _Algos.first { it.day == day && it.typeOfQuestion == typeOfQuestion }.algorythm
}
