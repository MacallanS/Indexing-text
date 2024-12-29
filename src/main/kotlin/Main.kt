import implementations.ThesaurusAnswerChooser
import interfaces.AnswerChooser
import utils.XmlSelector

fun main() {
    val answerChooser: AnswerChooser = ThesaurusAnswerChooser()

//    val query = "ГАРПУННАЯ ОХОТА"
//    val query = "вычитка текста"
    val query = "ГИЛЬЗА ОТ ПУЛИ"


    println(answerChooser.getAnswer(query))
}
