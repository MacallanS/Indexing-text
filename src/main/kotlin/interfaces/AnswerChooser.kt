package interfaces

interface AnswerChooser {
    fun getAnswer(question: String): String
    fun setThresholdValue(value: Double)
}

