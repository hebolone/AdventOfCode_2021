abstract class AlgosBase() {
    abstract fun Basic(input : MutableList<String>) : Any
    abstract fun Advanced(input : MutableList<String>) : Any
    var IsTest = false
    fun LoadTestDatas(uri : String) : MutableList<String> = Utils.GetInput(uri)
}