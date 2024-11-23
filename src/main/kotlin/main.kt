package main

import implementations.database.DAO
import implementations.StatisticAnswerChooser
import dataTypes.QueryTypes

fun main() {
    val dao = DAO()
    dao.openConnection("wordsdb.db")

    val question = "как дойти в библиотеку"
    val cleanedQuestion = question.replace(Regex("[^а-яА-Я\\s]"), "").split(" ")

    val answerChooser = StatisticAnswerChooser(dao)

    val queryResult = dao.queryExecute("SELECT question FROM questions_and_answers", "question", QueryTypes.SELECT)

    println("Пользовательский запрос (после очистки): $cleanedQuestion")
    
    println("Вопросы, хранящиеся в базе данных: $queryResult")

    val synonymsResult = dao.queryExecute("SELECT word FROM words", "word", QueryTypes.SELECT)
    println("Список синонимов в базе данных (после обновлений): $synonymsResult")

    val answer = answerChooser.getAnswer(question)
    println("Ответ на запрос: $answer")

    dao.closeConnection()
}
