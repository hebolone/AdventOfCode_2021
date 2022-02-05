package Days

import kotlin.math.abs

class Day17 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        return ParseInput(input, TREQUESTTYPE.BASIC)
    }

    override fun Advanced(input : MutableList<String>) : Int {
        //ParseInput(input)
        return ParseInput(input, TREQUESTTYPE.ADVANCED)
    }

    //region Private
    private data class Point(var p1 : Int, var p2 : Int) {
        override fun toString(): String = "[$p1, $p2]"
    }
    private data class TargetArea(val xRange : Point, val yRange : Point)
    private enum class TREQUESTTYPE { BASIC, ADVANCED }
    private enum class TRESULT { SHOOT, MISSED }

    private fun ParseInput(input : MutableList<String>, requestType : TREQUESTTYPE) : Int {
        var retValue = 0
        val regex = "target area: x=(?<x1>\\d+)\\.\\.(?<x2>\\d+), y=(?<y1>[-]\\d+)\\.\\.(?<y2>[-]\\d+)".toRegex()
        input.forEach {
            val match = regex.find(it)!!
            val (x1, x2, y1, y2) = match.destructured
            val xRange = Point(x1.toInt(), x2.toInt())
            val yRange = Point(y1.toInt(), y2.toInt())
            val targetArea = TargetArea(xRange, yRange)
            //val points = Shoot(Point(6, 0), targetArea, true)

            retValue = when(requestType) {
                TREQUESTTYPE.BASIC -> Summation(1, abs(yRange.p1) - 1)
                TREQUESTTYPE.ADVANCED -> Calculate(xRange, yRange, targetArea)
            }
        }
        return retValue
    }

    private fun Shoot(velocity : Point, targetArea : TargetArea, isDebug : Boolean = false) : TRESULT {
        val currentPosition = Point(0,0)
        var xVelocity = velocity.p1
        var yVelocity = velocity.p2
        val points = mutableListOf<Point>()
        var stopMovement = false
        var maximumY = 0
        var retValue = TRESULT.MISSED
        while(!stopMovement ) {
            val isInTargetArea = currentPosition.IsInTargetArea(targetArea)
            val isOutside = currentPosition.p1 >= targetArea.xRange.p2 || currentPosition.p2 <= targetArea.yRange.p1
            if(isInTargetArea)
                retValue = TRESULT.SHOOT
            stopMovement = isInTargetArea || isOutside
            points.add(currentPosition.Clone())
            currentPosition.ApplyVelocity(xVelocity, yVelocity)
            xVelocity += when {
                xVelocity == 0 -> 0
                xVelocity > 0 -> -1
                xVelocity < 0 -> 1
                else -> throw Exception("Invalid x position")
            }
            yVelocity += -1
            if(currentPosition.p2 > maximumY) maximumY = currentPosition.p2
        }

        if(isDebug) {
            println("Iterator : ${velocity.toString()}, result: $retValue")
            println("Result: ${points.PrintSequence()}")
        }

        return retValue
    }

    private fun Summation(from : Int, to : Int) : Int {
        var retValue = 0
        var counter = from
        while(counter <= to) {
            retValue += counter
            counter ++
        }
        return retValue
    }

    private fun Point.IsInTargetArea(targetArea : TargetArea) : Boolean = p1 in targetArea.xRange.p1..targetArea.xRange.p2 && p2 in targetArea.yRange.p1..targetArea.yRange.p2

    private fun Point.ApplyVelocity(xVelocity : Int, yVelocity : Int) {
        p1 += xVelocity
        p2 += yVelocity
    }

    private fun Point.Clone() = Point(p1, p2)

    private fun MutableList<Point>.PrintSequence() = joinToString { it.toString() }

    private fun Calculate(xRange : Point, yRange : Point, targetArea : TargetArea) : Int {
        var retValue = 0

        //  ALl other shoots
        (1 until (xRange.p2 + 1)).forEach { x ->
            (yRange.p1 until abs(yRange.p1)).forEach { y ->
                run {
                    if (Shoot(Point(x, y), targetArea) == TRESULT.SHOOT) {
                        retValue++
                    }
                }
            }
        }

        return retValue
    }
    //endregion
}