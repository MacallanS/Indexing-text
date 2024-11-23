package interfaces

import utils.SortInput

interface StatisticDataRequest {
    fun setConnection(dao: DAOConnection)
    fun findAnalogue(sorter: SortInput, input: String): List<String>?
    fun setNewWord(input: List<String>): String?
}


