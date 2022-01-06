import java.io.File

class Utils {
    companion object {
        val FilePattern = "/home/simone/Scrivania/AdventOfCode/day_%day%.txt"
        val FilePatternTest = "/home/simone/Scrivania/AdventOfCode/day_%day%_test.txt"

        private fun GetCorrectNumber(value : Int) : String = if(value < 10) "0${value}" else value.toString()

        fun GetInputFileName(day : Int, isTest : Boolean = false) : String {
            val filePattern = if(isTest) FilePatternTest else FilePattern
            return filePattern.replace("%day%", GetCorrectNumber(day))
        }

        fun GetInput(uri : String) : MutableList<String>  {
            val retValue = mutableListOf<String>()
            File(uri).forEachLine { retValue.add(it) }
            return retValue
        }
    }
}