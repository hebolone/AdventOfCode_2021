abstract class AlgosBase(isTest : Boolean = false) {
    abstract fun Basic(input : MutableList<String>) : Int
    abstract fun Advanced(input : MutableList<String>) : Int
    var IsTest = isTest
}