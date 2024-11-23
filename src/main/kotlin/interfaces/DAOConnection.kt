package interfaces

import dataTypes.QueryTypes

interface DAOConnection {
    fun openConnection(dbName: String): Boolean
    fun closeConnection(): Boolean
    fun queryExecute(query: String, column: String, type: QueryTypes): List<String>?
}

