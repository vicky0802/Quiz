package com.example.quiz;

import android.provider.BaseColumns;

public final class QuizContract {

    private QuizContract() {
    }

    public static class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_OPTION1 = "opt1";
        public static final String COLUMN_OPTION2 = "opt2";
        public static final String COLUMN_OPTION3 = "opt3";
        public static final String COLUMN_OPTION4 = "opt4";
        public static final String COLUMN_ANSWER_NR = "opt1";
    }
}