package Days

import kotlin.text.last as last

class Day14 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : ULong {
        _TablePoints.clear()
        return DoWork(input, 10)
    }

    override fun Advanced(input : MutableList<String>) : ULong {
        _TablePoints.clear()
        return DoWork(input, 40)
    }

    //region Private
    private data class Transformation(val code : String, val inserted : String)
    private val _Transformations = mutableListOf<Transformation>()
    private val _TablePoints = mutableMapOf<Char, ULong>()

    private fun ParseInput(lines : MutableList<String>) : String {
        var starting : String = ""
        lines.forEachIndexed { index, s ->
            run {
                when (index) {
                    0 -> {
                        starting = s
                    }
                    1 -> {}
                    else -> {
                        val regex = "(\\w{2}) -> (\\w)".toRegex()
                        val matchResult = regex.find(s)!!
                        val (code, inserted) = matchResult.destructured
                        _Transformations.add(Transformation(code, inserted))
                    }
                }
            }
        }
        return starting
    }

    private fun DoWork(input : MutableList<String>, iterations : Int) : ULong {
        var encodingString = ParseInput(input)
        var mappedInput = encodingString.ToCodeMap()

        //  Prepare table of points
        encodingString.forEach {
            _TablePoints.AddPoint(it, 1u)
        }

        //  Iterations
        (1..iterations).forEach { _ ->
            mappedInput = EncodeAdvanced(mappedInput)
        }

        return _TablePoints.maxOf { it.value } - _TablePoints.minOf { it.value }
    }

    private fun EncodeAdvanced(currentPairs: MutableMap<String, ULong>) : MutableMap<String, ULong> {
        var pairs = currentPairs.toMutableMap()
        currentPairs.forEach { (code, count) ->
            //  This couple of char generates 2 couples of char
            ApplyTransformation(code, count, pairs)
        }
        return pairs
    }

    private fun ApplyTransformation(code : String, count : ULong, pairs : MutableMap<String, ULong>) : Map<String, ULong> {
        val transformation = _Transformations.firstOrNull { it.code == code }
        if(transformation != null) {
            val firstCode = "${transformation.code.first()}${transformation.inserted}"
            val secondCode = "${transformation.inserted}${transformation.code.last()}"
            pairs.AddCode(firstCode, count)
            pairs.AddCode(secondCode, count)
            pairs.RemoveCode(code, count)
            _TablePoints.AddPoint(transformation.inserted.first(), count)
        } else {
            pairs.AddCode(code, count)
        }
        return pairs
    }

    private fun MutableMap<String, ULong>.AddCode(code : String, count : ULong) {
        val found = this.containsKey(code)
        if(found)
            this[code] = this[code]?.plus(count)!!
        else
            this[code] = count
    }

    private fun MutableMap<String, ULong>.RemoveCode(code : String, count : ULong) {
        val found = this.containsKey(code)
        if(found) {
            this[code] = this[code]?.minus(count)!!
        }
    }

    private fun MutableMap<Char, ULong>.AddPoint(char : Char, count : ULong) {
        if(this.containsKey(char))
            this[char] = this[char]?.plus(count)!!
        else
            this[char] = count
    }

    private fun String.ToCodeMap() : MutableMap<String, ULong> {
        val retValue = mutableMapOf<String, ULong>()
        var index = 1
        while(index < this.length) {
            val sub = this.subSequence(index - 1, index + 1).toString()
            retValue[sub] = 1u
            index++
        }
        return retValue
    }
    //endregion
}