package Days

class Day02 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        var horizontal = 0
        var depth = 0
        input.forEach {
            val movement = Parser(it)
            when(movement.direction) {
                TDirection.DOWN -> depth += movement.unit
                TDirection.UP -> depth -= movement.unit
                TDirection.FORWARD -> horizontal += movement.unit
            }
        }
        return horizontal * depth
    }

    override fun Advanced(input : MutableList<String>) : Int {
        var horizontal = 0
        var aim = 0
        var depth = 0
        input.forEach {
            val movement = Parser(it)
            when(movement.direction) {
                TDirection.DOWN -> aim += movement.unit
                TDirection.UP -> aim -= movement.unit
                TDirection.FORWARD -> {
                    horizontal += movement.unit
                    depth += aim * movement.unit
                }
            }
        }
        return horizontal * depth
    }

    //region Private
    private enum class TDirection {
        FORWARD, DOWN, UP
    }
    private fun Parser(value : String) : Movement {
        val splitted = value.split(" ")
        return Movement(ParseDirection(splitted[0]), splitted[1].toInt())
    }

    private fun ParseDirection(direction : String) : TDirection = TDirection.valueOf(direction.uppercase())

    private data class Movement(val direction : TDirection, val unit : Int)
    //endregion
}
