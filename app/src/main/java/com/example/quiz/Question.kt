package com.example.quiz

class Question {
    @JvmField
    var question: String? = null
    @JvmField
    var option1: String? = null
    @JvmField
    var option2: String? = null
    @JvmField
    var option3: String? = null
    @JvmField
    var option4: String? = null
    @JvmField
    var answerNr: String? = null

    constructor()
    constructor(
        question: String?,
        option1: String?,
        option2: String?,
        option3: String?,
        option4: String?,
        answerNr: String?
    ) {
        this.question = question
        this.option1 = option1
        this.option2 = option2
        this.option3 = option3
        this.option4 = option4
        this.answerNr = answerNr
    }
}