package implementations

import interfaces.AnswerChooser
import interfaces.Normalizer
import utils.NormalizerImpl
import utils.XmlSelector

/**
 * Класс выбора ответа
 * Алгоритм выбора ответа:
 * 1. Нормализация запроса (как пройти в библиотеку) -> (как пройти библиотеку)
 * 2. Разбиваем ввод пользователя на части по два слова (как пройти библиотеку) -> (как пройти, пройти библиотеку)
 * 3. Ищем в тезаурусе подходящие строки по тегу "name" и достаем из него строку по тегу "lemma"
 * 4. Удаляем повторяющиеся слова
 * 5. Если в базе данных есть строка с набором лемм, соответствующих получившемуся массиву - выводим ответ
 */
class ThesaurusAnswerChooser : AnswerChooser {
    private val senses by lazy { XmlSelector("/thesaurus/senses.xml") }
    private val serviceWords by lazy { XmlSelector("/thesaurus/service_words.xml") }

    /**
     * Фильтрация результатов по ключевым словам из вопроса
     */
    private fun filterResults(question: String, results: List<String>): List<String> {
        println("Фильтрация результатов для вопроса: $question")
        println("Изначальные результаты: $results")

        val keywords = question.split(" ").map { it.uppercase() }

        // Поиск точных совпадений
        val exactMatch = results.filter { it.uppercase() == question.uppercase() }
        println("Точные совпадения: $exactMatch")

        // Поиск совпадений по ключевым словам
        val keywordMatches = results.filter { result ->
            keywords.any { keyword -> result.uppercase() == keyword }
        }
        println("Совпадения по ключевым словам: $keywordMatches")

        // Объединяем результаты и удаляем дубликаты
        val filtered = (exactMatch + keywordMatches).distinct()
        println("Отфильтрованные результаты: $filtered")
        return filtered
    }

    /**
     * Обработка одной строки текста (двуслова или слова)
     */
    private fun processText(text: String): List<String> {
        println("Обработка текста: $text")
        val results = senses.getAttributeValuesByAttributeNameAndAttributeValue(
            attributeName = "name",
            attributeValue = text.uppercase(),
            attributeValueName = "lemma"
        )
        println("Результаты поиска для '$text': $results")
        return filterResults(text, results)
    }

    override fun getAnswer(question: String): String {
        println("Получен вопрос: $question")
        if (question.isBlank()) {
            println("Вопрос пустой. Возвращаем стандартный ответ.")
            return "Вопрос не задан."
        }

        val normalizer: Normalizer = NormalizerImpl(question, serviceWords)
        normalizer.normalize(question)
        println("После нормализации: ${normalizer}")

        val phrases = normalizer.splitter()
        println("Разбитые фразы: $phrases")

        val finalLemmas = mutableListOf<String>()

        phrases.forEach { text ->
            println("Обработка фразы: $text")
            // Обрабатываем фразу или двусловие
            val lemmas = processText(text)

            if (lemmas.isNotEmpty()) {
                println("Леммы, найденные для фразы '$text': $lemmas")
                finalLemmas.addAll(lemmas)
            } else {
                println("Леммы не найдены для фразы '$text'. Переход к обработке слов.")
                // Если нет результата, разбиваем на слова и обрабатываем по отдельности
                text.split(" ").forEach { word ->
                    val wordLemmas = processText(word)
                    if (wordLemmas.isEmpty()) {
                        println("Леммы не найдены для слова '$word'. Добавляем слово в результат.")
                        finalLemmas.add(word)
                    } else {
                        println("Леммы, найденные для слова '$word': $wordLemmas")
                        finalLemmas.addAll(wordLemmas)
                    }
                }
            }
        }

        // Удаляем дубликаты и возвращаем результат
        val finalResult = finalLemmas.distinct().joinToString(" ")
        println("Итоговый результат: $finalResult")
        return finalResult
    }
}