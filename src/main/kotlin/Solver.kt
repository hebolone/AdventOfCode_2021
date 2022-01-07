class Solver  {
    private val _Algos = Algos()
    private val _Tests = mutableListOf<Int>()

    fun Solve(day : Int, typeOfQuestion : Algos.TYPEOFQUESTION) : String? {
        var retValue : String? = null

        //  Choose algorhythm
        val algorithm = _Algos.GetAlgo(day, typeOfQuestion)

        //  Choose data files
        val isTest = _Tests.contains(day)
        val fileName = Utils.GetInputFileName(day, isTest)

        //  Get Input
        if(algorithm != null) retValue = algorithm(Utils.GetInput(fileName)).toString()

        return retValue
    }

    fun AddTestDay(day : Int) = _Tests.add(day)
}