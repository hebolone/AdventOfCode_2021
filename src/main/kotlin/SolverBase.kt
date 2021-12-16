import java.io.File

abstract class SolverBase {


    abstract fun Solve(day : Int, typeOfQuestion : Algos.TYPEOFQUESTION) : String?



    /*protected fun GetInput(uri : String) : MutableList<String>  {
        val retValue = mutableListOf<String>()
        File(uri).forEachLine { retValue.add(it) }
        return retValue
    }*/
}

class Solver : SolverBase() {
    val _Algos = Algos()

    override fun Solve(day : Int, typeOfQuestion : Algos.TYPEOFQUESTION) : String? {
        var retValue : String? = null

        //  Choose algorhythm
        val algorythm = _Algos.GetAlgo(day, typeOfQuestion)

        //  Get Input
        if(algorythm != null) retValue = algorythm(Utils.GetInput(Utils.GetInputFileName(day))).toString()

        return retValue
    }
}