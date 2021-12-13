import java.io.File

abstract class SolverBase {
    private val _InputFile = "/home/simone/Scrivania/AdventOfCode/day_%day%.txt"

    abstract fun Solve(day : Int, typeOfQuestion : Algos.TYPEOFQUESTION) : Int?

    protected fun GetInputFileName(day : Int) : String = _InputFile.replace("%day%", Utils.GetCorrectNumber(day))

    protected fun GetInput(uri : String) : MutableList<String>  {
        val retValue = mutableListOf<String>()
        File(uri).forEachLine { retValue.add(it) }
        return retValue
    }
}

class Solver : SolverBase() {
    val _Algos = Algos()

    override fun Solve(day : Int, typeOfQuestion : Algos.TYPEOFQUESTION) : Int? {
        var retValue : Int? = null

        //  Choose algorhythm
        val result = _Algos.GetAlgo(day, typeOfQuestion)

        //  Get Input
        if(result != null) retValue = result(GetInput(GetInputFileName(day)))

        return retValue
    }
}