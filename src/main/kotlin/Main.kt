fun main(args: Array<String>) {
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.

    val s = Solver()
    //val lastDay = 31
    val results = mutableListOf<Day>()

    //  Single question
    val day = 4
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
        println("Day: ${it.day} (${it.typeOfQuestion}) = ${it.result}")
    }
}

data class Day(val day : Int, val typeOfQuestion : Algos.TYPEOFQUESTION, var result : Int)

fun MutableList<Day>.addIfPresent(day : Int, typeOfQuestion : Algos.TYPEOFQUESTION, result : Int?) {
    if(result != null) this.add(Day(day, typeOfQuestion, result))
}
