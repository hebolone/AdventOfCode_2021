class Board<T>(val x : Int, val y : Int, val initializer : () -> T) {
    private val _Datas : MutableList<T> = mutableListOf()
    init {
        (1..(x * y)).forEach { _ -> _Datas.add(initializer()) }
    }
    fun Get(xFrom : Int, yFrom : Int) : T = _Datas[xFrom + yFrom * x]
    fun Set(xFrom : Int, yFrom : Int, value : T) { _Datas[xFrom + yFrom * x] = value }
    fun GetLinear(value : Int) : T = _Datas[value]
    fun GetSize() : Int = x * y
    fun PrintBoard(printFun : (t : T) -> String = { it.toString() }) : String {
        val sb = StringBuilder()
        (0 until y).forEach { _y -> run {
            (0 until x).forEach { _x ->
                sb.append(printFun(Get(_x, _y)))
            }
            sb.appendLine()
        } }
        return sb.toString()
    }
}