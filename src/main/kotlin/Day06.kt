class Day06 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        val datas = if(IsTest) _InputTest.split(",").toMutableList() else input.first().split(",").toMutableList()
        val result = Iterate(datas.map { it.toInt() }.toMutableList(), 80)

        return result.count()
    }

    override fun Advanced(input : MutableList<String>) : Int {
        val datas = if(IsTest) _InputTest.split(",").toMutableList() else input.first().split(",").toMutableList()
        val result = Iterate(datas.map { it.toInt() }.toMutableList(), 256)

        return result.count()
    }

    //region Private
    private val _InputTest = "3,4,3,1,2"

    private fun Iterate(input : MutableList<Int>, day : Int, cycle : Int = 1) : MutableList<Int> {
        var retValue = mutableListOf<Int>()
        var newFish = 0
        input.forEach {
            //  Apply rules
            when(it) {
                0 -> {
                    retValue.add(6)
                    newFish ++
                }
                else -> retValue.add(it - 1)
            }
        }
        (1..newFish).forEach { retValue.add(8) }

        return if(cycle < day) Iterate(retValue, day, cycle + 1) else retValue
    }
    //endregion
}