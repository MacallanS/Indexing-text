package implementations.database

import interfaces.DAOConnection
import dataTypes.QueryTypes
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class DAO : DAOConnection {
    private var connection: Connection? = null

    fun addSynonyms(word: String, synonyms: List<String>) {
        synonyms.forEach { synonym ->
            queryExecute("INSERT INTO synonyms (word, synonym) VALUES ('$word', '$synonym')", "", QueryTypes.INSERT)
        }
    }

    fun getSynonymsForWord(word: String): List<String> {
        return queryExecute("SELECT synonym FROM synonyms WHERE word = '$word'", "synonym", QueryTypes.SELECT) ?: listOf(word)
    }

    override fun openConnection(dbName: String): Boolean {
        return try {
            connection = DriverManager.getConnection("jdbc:sqlite:$dbName")
            println("Connection to SQLite has been established.")
            true
        } catch (e: Exception) {
            println("Error opening connection: ${e.message}")
            false
        }
    }

    override fun closeConnection(): Boolean {
        return try {
            connection?.close()
            println("Connection closed.")
            true
        } catch (e: Exception) {
            println("Error closing connection: ${e.message}")
            false
        }
    }

    override fun queryExecute(query: String, column: String, type: QueryTypes): List<String>? {
        val statement = connection?.createStatement()
        return try {
            val resultSet: ResultSet? = statement?.executeQuery(query)
            val results = mutableListOf<String>()
            while (resultSet?.next() == true) {
                val value = resultSet.getString(column)
                if (value != null) {
                    results.add(value)
                }
            }
            results.ifEmpty { null }
        } catch (e: Exception) {
            println("SQL error: ${e.message}")
            null
        }
    }
    
}


