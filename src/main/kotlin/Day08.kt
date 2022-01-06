class Day08 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int = ParseInputBasic(input)

    override fun Advanced(input : MutableList<String>) : Int = ParseInputAdvanced(input)

    //region Private
    private fun ParseInputBasic(input : MutableList<String>) : Int {
        var retValue = 0
        input.forEach {
            val digits = ParseSingleLine(it)
            retValue += digits.drop(10).filter { it.length == 2 || it.length == 3 || it.length == 4 || it.length == 7 }.count()
        }
        return retValue
    }

    private fun ParseInputAdvanced(input : MutableList<String>) : Int {
        var retValue = 0
        for(line in input) {
            val allDigits = AllDigits()
            val digits = ParseSingleLine(line)
            (0..9).forEach {
                val digit = Digit(digits[it])
                allDigits.add(digit)
            }

            //  Search for 3 (1 and sequence length of 5)
            allDigits.Digits.filter { it.Segments.count() == 5 }.forEach {
                if(allDigits[1]!!.Segments.OperationAnd(it.Segments).OperationEquals(allDigits[1]!!.Segments))
                    it.Value = 3
            }

            //  Search for 6 (1 not 6 length results in a segment of 1 length)
            var correctSequence2 = ' '
            allDigits.Digits.filter { it.Segments.count() == 6 }.forEach {
                val resultOperation = allDigits[1]!!.Segments.OperationNot(it.Segments)
                if(resultOperation.count() == 1) {
                    it.Value = 6
                    correctSequence2 = resultOperation.first()
                }
            }

            //  Search for 2 (5 length and sequence[2]
            allDigits.Digits.filter { it.Segments.count() == 5 && it.Value == null }.forEach {
                val c : CharArray = charArrayOf(correctSequence2)
                val resultOperation = it.Segments.OperationAnd(c)
                if(resultOperation.count() == 1) {
                    it.Value = 2
                }
            }

            //  Search for 5 (5 length left)
            allDigits.Digits.first { it.Segments.count() == 5 && it.Value == null }.Value = 5

            //  Search for 9 (4 not 1 and 6 length
            val result4not1 = allDigits[4]!!.Segments.OperationNot(allDigits[1]!!.Segments)
            allDigits.Digits.filter { it.Segments.count() == 6 && it.Value == null }.forEach {
                val resultOperation = result4not1.OperationNot(it.Segments)
                if(resultOperation.isEmpty())
                    it.Value = 9
            }

            //  Search for 0 (only value left!)
            allDigits.Digits.first { it.Value == null }.Value = 0

            //  Ok, now I have all digits
            //  Let's convert last 4
            var resultString = ""
            (10..13).forEach {
                val result = allDigits.SearchFromSegment(digits[it])
                resultString += result?.Value.toString()
            }
            retValue += resultString.toInt()
        }

        return retValue
    }

    private fun ParseSingleLine(input : String) : List<String> {
        val regex = "([abcdefg]{1,7})".toRegex()
        val matchResult = regex.findAll(input)
        return matchResult.map{ it.value }.toList()
    }

    private class Digit(val segments : String) {
        private var _Segments : CharArray = segments.toCharArray().sortedArray()
        init {
            AssignValue()
        }
        private fun AssignValue() {
            when(segments.length) {
                2 -> Value = 1
                3 -> Value = 7
                4 -> Value = 4
                7 -> Value = 8
            }
        }

        var Value : Int? = null
        val Segments = _Segments
    }

    private class AllDigits {
        private val _Digits = mutableListOf<Digit>()
        val Digits = _Digits

        fun add(digit : Digit) = _Digits.add(digit)

        fun SearchFromSegment(segments : String) : Digit? {
            val s = segments.toCharArray().sortedArray()
            return _Digits.firstOrNull { it.Segments.contentEquals(s) }
        }

        operator fun get(i : Int) : Digit? = _Digits.firstOrNull { it.Value == i }
    }

    private fun CharArray.OperationAnd(d2 : CharArray) : CharArray = this.intersect(d2.asIterable()).toCharArray()
    private fun CharArray.OperationNot(d2 : CharArray) : CharArray = this.filter { ! d2.contains(it) }.toCharArray()
    private fun CharArray.OperationOr(d2 : CharArray) : CharArray {
        var retValue = mutableSetOf<Char>()
        retValue.addAll(this.toSet())
        retValue.addAll(d2.toSet())
        return retValue.toCharArray()
    }
    private fun CharArray.OperationEquals(d2 : CharArray) : Boolean = this.contentEquals(d2)
    //endregion
}