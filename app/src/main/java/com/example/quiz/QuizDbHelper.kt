package com.example.quiz

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.quiz.QuizContract.QuestionsTable
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class QuizDbHelper(private val context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    private var db: SQLiteDatabase? = null
    override fun onCreate(db: SQLiteDatabase) {
        this.db = db

//        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
//                QuestionsTable.TABLE_NAME + " ( " +
//                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
//                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
//                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
//                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
//                QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
//                QuestionsTable.COLUMN_ANSWER_NR + " TEXT" +
//                ")";
//
//        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
//        createDatabase();
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME)
        onCreate(db)
    }

    fun createDatabase() {
        val dbExist = checkDatabase()
        if (!dbExist) {
            this.readableDatabase
            copyDatabase()
        }
    }

    private fun checkDatabase(): Boolean {
        var checkDB: SQLiteDatabase? = null
        try {
            val dbPath = context.getDatabasePath(DATABASE_NAME).path
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)
        } catch (e: Exception) {
            // Database does not exist yet.
        }
        checkDB?.close()
        return checkDB != null
    }

    private fun copyDatabase() {
        try {
            val inputStream = context.assets.open(DATABASE_NAME)
            val outFileName = context.getDatabasePath(DATABASE_NAME).path
            val outputStream: OutputStream = FileOutputStream(outFileName)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    val allQuestions: List<Question>
        get() {
            val questionList: MutableList<Question> = ArrayList()
            db = readableDatabase
            val c = db?.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null)
            if (c?.moveToFirst() == true) {
                do {
                    val question = Question()
                    question.question =
                        c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION))
                    question.option1 = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1))
                    question.option2 = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2))
                    question.option3 = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3))
                    question.option4 = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4))
                    question.answerNr =
                        c.getString(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR))
                    questionList.add(question)
                } while (c.moveToNext())
            }
            c?.close()
            return questionList
        }

    companion object {
        //    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
        private const val DATABASE_NAME = "database.sqlite"
        private const val DATABASE_VERSION = 1
    }
}