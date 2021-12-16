import java.io.File

class Utils {
    companion object {
        val FilePattern = "/home/simone/Scrivania/AdventOfCode/day_%day%.txt"

        fun GetCorrectNumber(value : Int) : String = if(value < 10) "0${value}" else value.toString()

        fun GetInputFileName(day : Int) : String = Utils.FilePattern.replace("%day%", Utils.GetCorrectNumber(day))

        fun GetInput(uri : String) : MutableList<String>  {
            val retValue = mutableListOf<String>()
            File(uri).forEachLine { retValue.add(it) }
            return retValue
        }
    }
}