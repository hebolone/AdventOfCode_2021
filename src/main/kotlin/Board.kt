open class Board<T>(val x : Int, val y : Int, initializer : () -> T) /*: Iterable<T>*/ {
    protected val _Cells : MutableList<T> = mutableListOf()
    protected val _Initializer = initializer
    init {
        (1..(x * y)).forEach { _ -> _Cells.add(initializer()) }
    }
    fun GetLinear(value : Int) : T = _Cells[value]
    fun GetSize() : Int = x * y
    fun PrintBoard(printFun : (t : T) -> String = { it.toString() }) : String {
        val sb = StringBuilder()
        (0 until y).forEach { _y -> run {
            (0 until x).forEach { _x ->
                sb.append(printFun(this[_x, _y]))
            }
            sb.appendLine()
        } }
        return sb.toString()
    }
    //  Indexer
    open operator fun get(xFrom : Int, yFrom : Int) : T = _Cells[xFrom + yFrom * x]
    open operator fun get(value : Int) : T = _Cells[value]
    open operator fun set(xFrom : Int, yFrom : Int, value : T) { _Cells[xFrom + yFrom * x] = value }
    open operator fun set(index : Int, value : T) { _Cells[index] = value }
    fun GetCoordinatesFromIndex(index : Int) : Pair<Int, Int> {
        val y_derived = index / y
        val x_derived = index - (y * y_derived)
        return Pair(x_derived, y_derived)
    }
    fun Slice(newX : Int, newY : Int) : Board<T> {
        val retValue = Board(newX, newY, _Initializer)
        (0 until newX).forEach { oldX ->
            run {
                (0 until newY).forEach { oldY ->
                    run {
                        retValue[oldX, oldY] = this[oldX, oldY]
                    }
                }
            }
        }
        return retValue
    }
}

class BoardExtended<T>(xExt : Int, yExt : Int, initializerExt : () -> T) : Board<T>(xExt, yExt, initializerExt) {
    fun GetOrNull(xFrom : Int, yFrom : Int) : T? {
        val index = xFrom + yFrom * x
        return if(xFrom < 0 || xFrom >= x || yFrom < 0 || yFrom >= y)
            null
        else
            _Cells[index]
    }
}