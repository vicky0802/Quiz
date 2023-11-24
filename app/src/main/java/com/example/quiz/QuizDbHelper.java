package com.example.quiz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import com.example.quiz.QuizContract.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class QuizDbHelper extends SQLiteOpenHelper {
//    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final String DATABASE_NAME = "database.sqlite";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;
    private final Context context;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    public void createDatabase() {
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            String dbPath = context.getDatabasePath(DATABASE_NAME).getPath();
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (Exception e) {
            // Database does not exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    private void copyDatabase() {
        try {
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            String outFileName = context.getDatabasePath(DATABASE_NAME).getPath();
            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Question>   getAllQuestions() {
        List<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}