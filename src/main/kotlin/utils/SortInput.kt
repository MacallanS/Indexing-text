package utils

import utils.InputUnit

class SortInput(private val unit: InputUnit) {
    private var result: List<String>? = unit.getData()?.distinct()
    private var frequency = result?.size?.let { IntArray(it) { 1 } }

    init {
        sort()
    }

    private fun sort() {
        val data = unit.getData()
        if (data != null && result != null && frequency != null) {
            for ((count, i) in result!!.withIndex()) {
                if (i == data[count])
                    frequency!![count]++
            }
        }
    }

    fun getUniq() = result
    fun getFreq() = frequency
}
