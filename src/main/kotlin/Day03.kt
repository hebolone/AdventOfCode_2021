class Day03 : AlgosBase()  {
    override fun Basic(input : MutableList<String>) : Int {
        val gammaRateResult  = CalculateGammaRate(input)
        val epsilonRateResult = InvertBinaryNumber(gammaRateResult)

        return Integer.parseInt(gammaRateResult, 2) * Integer.parseInt(epsilonRateResult, 2)
    }

    override fun Advanced(input : MutableList<String>) : Int {
        val oxygenRating = GetOxygenRating(input)
        val co2Rating = GetCO2Rating(input)

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

    private fun GetOxygenRating(input : MutableList<String>, cycle : Int = 0) : MutableList<String> {
        val gammaResult = CalculateGammaRate(input)
        val retValue = mutableListOf<String>()
        input.forEach { if(it[cycle] == gammaResult[cycle]) retValue.add(it) }
        //return if(retValue.count() == 1) retValue else GetOxygenRating(retValue, cycle + 1)
        return when(retValue.count()) {
            0 -> GetOxygenRating(input, cycle + 1)
            1 -> retValue
            else -> GetOxygenRating(retValue, cycle + 1)
        }
    }

    private fun GetCO2Rating(input : MutableList<String>, cycle : Int = 0) : MutableList<String> {
        val gammaResultInverted = InvertBinaryNumber(CalculateGammaRate(input))
        val retValue = mutableListOf<String>()
        input.forEach { if(it[cycle] == gammaResultInverted[cycle]) retValue.add(it) }
        return when(retValue.count()) {
            0 -> GetCO2Rating(input, cycle + 1)
            1 -> retValue
            else -> GetCO2Rating(retValue, cycle + 1)
        }
    }

    private fun InvertBinaryNumber(number : String) : String {
        var retValue = ""
        number.forEach { retValue += if(it == '1') 0 else 1 }
        return retValue
    }
    //endregion
}