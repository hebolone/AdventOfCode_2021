class Day10 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        val datas = if(IsTest) LoadTestDatas("/home/simone/Scrivania/AdventOfCode/day_10_test.txt") else input
        _Solution = ParseLines(datas)!!
        return _Solution!!.basic
    }

    override fun Advanced(input : MutableList<String>) : ULong {
        val datas = if(IsTest) LoadTestDatas("/home/simone/Scrivania/AdventOfCode/day_10_test.txt") else input
        if(_Solution == null)
            _Solution = ParseLines(datas)
        return _Solution!!.advanced
    }

    //region Private
    private data class SymbolDefinition(val opening : Char, val closing : Char, val corruptionPoint : Int, val completionPoint : Int)
    private data class ResultParsing(val corruptionPoints : Int, val missingString : String?)
    private data class Solution(val basic : Int, val advanced : ULong)
    private val _Definitions = listOf(
        SymbolDefinition('<', '>', 25137, 4),
        SymbolDefinition('{', '}', 1197, 3),
        SymbolDefinition('[', ']', 57, 2),
        SymbolDefinition('(', ')', 3, 1)
    )
    private val _Stack = ArrayDeque<SymbolDefinition>()
    private var _Solution : Solution? = null

    private fun ParseLines(input : MutableList<String>) : Solution {
        var corruptionPoints = 0
        var advancedPoints = mutableListOf<ULong>()
        input.forEach {
            val result = ParseSingleLine(it)
            corruptionPoints += result.corruptionPoints

            val advancedResult = CalculateAdvancedPoints(result.missingString ?: "")
            if(advancedResult > 0u)
                advancedPoints.add(advancedResult)
        }

        //  Calculate middle score
        advancedPoints.sort()
        val middleValue = advancedPoints.size / 2

        return Solution(corruptionPoints, advancedPoints[middleValue])
    }

    private fun ParseSingleLine(line: String) : ResultParsing {
        val _stack = ArrayDeque<SymbolDefinition>()
        var corruptionPoints = 0
        line.forEach {
            when(it) {
                '<' -> _stack.addLast(_Definitions[0])
                '{' -> _stack.addLast(_Definitions[1])
                '[' -> _stack.addLast(_Definitions[2])
                '(' -> _stack.addLast(_Definitions[3])
                ')' -> if(_stack.last().closing == ')') _stack.removeLast() else corruptionPoints = AddCorruptionPoint(corruptionPoints, _Definitions.GetSymbol(')').corruptionPoint)
                ']' -> if(_stack.last().closing == ']') _stack.removeLast() else corruptionPoints = AddCorruptionPoint(corruptionPoints, _Definitions.GetSymbol(']').corruptionPoint)
                '}' -> if(_stack.last().closing == '}') _stack.removeLast() else corruptionPoints = AddCorruptionPoint(corruptionPoints, _Definitions.GetSymbol('}').corruptionPoint)
                '>' -> if(_stack.last().closing == '>') _stack.removeLast() else corruptionPoints = AddCorruptionPoint(corruptionPoints, _Definitions.GetSymbol('>').corruptionPoint)
            }
        }

        var missingString : String? = null
        if(corruptionPoints == 0) {
            missingString = GetMissingString(_stack)
            //println("Missing string: $missingString")
        }

        return ResultParsing(corruptionPoints, missingString)
    }

    private fun List<SymbolDefinition>.GetSymbol(char : Char) : SymbolDefinition = this.firstOrNull { it.closing == char }!!

    private fun AddCorruptionPoint(cp : Int, points : Int) = if(cp == 0) points else cp

    private fun GetMissingString(stack : ArrayDeque<SymbolDefinition>) : String {
        var retValue = ""
        while(stack.isNotEmpty()) {
            val symbol = stack.removeLast()
            retValue += symbol.closing
        }
        return retValue
    }

    private fun CalculateAdvancedPoints(missingString : String) : ULong {
        var retValue : ULong = 0u
        missingString.forEach {
            retValue *= 5u
            retValue += _Definitions.GetSymbol(it).completionPoint.toULong()
        }
        return retValue
    }
    //endregion
}