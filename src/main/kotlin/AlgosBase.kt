abstract class AlgosBase(isTest : Boolean = false) {
    abstract fun Basic(input : MutableList<String>) : Any
    abstract fun Advanced(input : MutableList<String>) : Any
    var IsTest = isTest
}