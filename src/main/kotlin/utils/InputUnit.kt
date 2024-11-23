package utils

import dataTypes.Methods

class InputUnit(input: String, method: Methods = Methods.STATISTIC) {
    private var unit: List<String>? = input.split(' ')
    fun getData() = unit
}

