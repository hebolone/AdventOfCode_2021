class Day03 : AlgosBase()  {
    override fun Basic(input : MutableList<String>) : Int {
        val gammaRateResult  = CalculateGammaRate(input)
        val epsilonRateResult = InvertBinaryNumber(gammaRateResult)

        return Integer.parseInt(gammaRateResult, 2) * Integer.parseInt(epsilonRateResult, 2)
    }

    override fun Advanced(input : MutableList<String>) : Int {
        val oxygenRating = GetRating(input, TTypeOfRating.OXYGEN)
        val co2Rating = GetRating(input, TTypeOfRating.CO2)

        return Integer.parseInt(oxygenRating.first(), 2) * Integer.parseInt(co2Rating.first(), 2)
    }

    //region Private
    private fun CalculateGammaRate(input : MutableList<String>) : String {
        val gammaRate = IntArray(input.first().length)
        val totalDatas = input.count()
        input.forEach {
            it.toCharArray().forEachIndexed { index, c -> gammaRate[index] += Character.getNumericValue(c) }
        }
        var gammaRateResult = ""
        gammaRate.forEach { gammaRateResult += if(it >= (totalDatas - it)) "1" else "0" }
        return gammaRateResult
    }

    private enum class TTypeOfRating { OXYGEN, CO2 }

    private fun GetRating(input : MutableList<String>, typeOfRating : TTypeOfRating, cycle : Int = 0) : MutableList<String> {
        var gammaResult = CalculateGammaRate(input)
        if(typeOfRating == TTypeOfRating.CO2)
            gammaResult = InvertBinaryNumber(gammaResult)
        val retValue = mutableListOf<String>()
        input.forEach { if(it[cycle] == gammaResult[cycle]) retValue.add(it) }
        return when(retValue.count()) {
            0 -> GetRating(input, typeOfRating, cycle + 1)
            1 -> retValue
            else -> GetRating(retValue, typeOfRating, cycle + 1)
        }
    }

    private fun InvertBinaryNumber(number : String) : String {
        var retValue = ""
        number.forEach { retValue += if(it == '1') 0 else 1 }
        return retValue
    }
    //endregion
}