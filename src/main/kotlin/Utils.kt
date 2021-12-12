class Utils {
    companion object {
        fun GetCorrectNumber(value : Int) : String = if(value < 10) "0${value}" else value.toString()
    }
}