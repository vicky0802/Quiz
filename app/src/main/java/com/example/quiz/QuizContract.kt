package com.example.quiz

import android.provider.BaseColumns

class QuizContract private constructor() {
    object QuestionsTable : BaseColumns {
        const val TABLE_NAME = "quiz"
        const val COLUMN_QUESTION = "question"
        const val COLUMN_OPTION1 = "opt1"
        const val COLUMN_OPTION2 = "opt2"
        const val COLUMN_OPTION3 = "opt3"
        const val COLUMN_OPTION4 = "opt4"
        const val COLUMN_ANSWER_NR = "opt1"
    }
}