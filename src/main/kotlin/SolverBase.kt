class Solver  {
    val _Algos = Algos()

    fun Solve(day : Int, typeOfQuestion : Algos.TYPEOFQUESTION) : String? {
        var retValue : String? = null

        //  Choose algorhythm
        val algorithm = _Algos.GetAlgo(day, typeOfQuestion)

        //  Choose data files
        val fileName = Utils.GetInputFileName(day)

        //  Get Input
        if(algorithm != null) retValue = algorithm(Utils.GetInput(fileName)).toString()

        return retValue
    }
}