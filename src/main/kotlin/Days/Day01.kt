package Days

class Day01 : AlgosBase() {
    override fun Basic(input : MutableList<String>) : Int {
        var retValue = 0
        var buffer = input.first().toInt()
        input.drop(1).map{ it.toInt() }.forEach {
            if(it > buffer) retValue ++
            buffer = it
        }
        return retValue
    }

    override fun Advanced(input : MutableList<String>) : Int {
        val buffers = IntArray(2000)
        buffers[0] = input.first().toInt()
        buffers[0] += input[1].toInt()
        buffers[1] = input[1].toInt()
        input.drop(2).map{ it.toInt() }.forEachIndexed { index, i ->
            buffers[index + 2] = i
            buffers[index + 1] += i
            buffers[index] += i
        }

        return Basic(buffers.map{ it.toString() }.toMutableList())
    }
}