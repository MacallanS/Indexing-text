package implementations

import implementations.database.DAO
import interfaces.AnswerChooser
import dataTypes.QueryTypes

class StatisticAnswerChooser(private val dao: DAO) : AnswerChooser {

    private var thresholdValue: Double = 1.0
    val defaultAnswer = "Ответ найти не удалось, пожалуйста, повторите запрос."

    override fun getAnswer(question: String): String {
        val queryResult: List<String>? = dao.queryExecute("SELECT question FROM questions_and_answers", "question", QueryTypes.SELECT)

        val wordsInQuestion = question.replace(Regex("[^а-яА-Я\\s]"), "").split(" ")

        wordsInQuestion.forEach { word ->
            val synonyms = getSynonymsForWord(word)
            synonyms.forEach { synonym ->
                val synonymExists = dao.queryExecute("SELECT connected_word FROM words WHERE word = '$word' AND connected_word = '$synonym'", "connected_word", QueryTypes.SELECT)
                if (synonymExists.isNullOrEmpty()) {
                    dao.queryExecute("INSERT INTO words (word, connected_word) VALUES ('$word', '$synonym')", "", QueryTypes.INSERT)
                    println("Добавлено новое связанное слово: '$word' с синонимом '$synonym'.")
                }
            }
        }

        var bestMatch: String? = null
        var bestMatchCount = 0

        queryResult?.forEach { dbQuestion ->
            val matchCount = dbQuestion.split(" ").intersect(wordsInQuestion).size
            if (matchCount > bestMatchCount) {
                bestMatchCount = matchCount
                val answer = dao.queryExecute("SELECT answer FROM questions_and_answers WHERE question = '$dbQuestion'", "answer", QueryTypes.SELECT)?.firstOrNull()
                if (answer != null) {
                    bestMatch = answer
                }
            }
        }

        return bestMatch ?: defaultAnswer
    }

    private fun getSynonymsForWord(word: String): List<String> {
        val query = "SELECT connected_word FROM words WHERE word = '$word'"
        val list =  dao.queryExecute(query, "connected_word", QueryTypes.SELECT)

        return list?.toSet()?.toList() ?: listOf(word)
    }

    override fun setThresholdValue(value: Double) {
        thresholdValue = value
    }
}
