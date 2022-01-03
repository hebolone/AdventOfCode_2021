fun main(args: Array<String>) {
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.

    val s = Solver()
    //val lastDay = 31
    val results = mutableListOf<Day>()

    //  Single question
    val day = 11
    var typeOfQuestion = Algos.TYPEOFQUESTION.BASIC
    results.addIfPresent(day, typeOfQuestion, s.Solve(day, typeOfQuestion))
    typeOfQuestion = Algos.TYPEOFQUESTION.ADVANCED
    results.addIfPresent(day, typeOfQuestion, s.Solve(day, typeOfQuestion))

/*
    //  Multiple questions
    (1..lastDay).forEach {
        results.addIfPresent(it, Algos.TYPEOFQUESTION.BASIC, s.Solve(it, Algos.TYPEOFQUESTION.BASIC))
        results.addIfPresent(it, Algos.TYPEOFQUESTION.ADVANCED, s.Solve(it, Algos.TYPEOFQUESTION.ADVANCED))
    }
*/
    results.forEach {
        println(it.toPrintableString())
    }
}

data class Day(val day : Int, val typeOfQuestion : Algos.TYPEOFQUESTION, var result : String)

fun Day.toPrintableString() = "Day: $day (${typeOfQuestion}) = $result"

fun MutableList<Day>.addIfPresent(day : Int, typeOfQuestion : Algos.TYPEOFQUESTION, result : String?) {
    if(result != null) this.add(Day(day, typeOfQuestion, result))
}
