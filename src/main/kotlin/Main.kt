fun main() {
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.

    val s = Solver()
    val results = mutableListOf<Day>()

    //  Single question
    val day = 18
    s.AddTestDay(day)

    var typeOfQuestion = Algos.TYPEOFQUESTION.BASIC
    results.addIfPresent(day, typeOfQuestion, s.Solve(day, typeOfQuestion))
    typeOfQuestion = Algos.TYPEOFQUESTION.ADVANCED
    results.addIfPresent(day, typeOfQuestion, s.Solve(day, typeOfQuestion))

    results.forEach {
        println(it.toPrintableString())
    }
}

data class Day(val day : Int, val typeOfQuestion : Algos.TYPEOFQUESTION, var result : String)

fun Day.toPrintableString() = "Day: $day (${typeOfQuestion}) = $result"

fun MutableList<Day>.addIfPresent(day : Int, typeOfQuestion : Algos.TYPEOFQUESTION, result : String?) {
    if(result != null) this.add(Day(day, typeOfQuestion, result))
}
