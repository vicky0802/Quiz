package com.example.quiz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.databinding.ActivityQuizBinding;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    public static final String FUll_SCORE = "fullScore";
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    ActivityQuizBinding binding;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private List<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        textColorDefaultRb = binding.optionA.getTextColors();
        textColorDefaultCd = binding.textViewCountdown.getTextColors();

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        dbHelper.createDatabase();
        questionList = dbHelper.getAllQuestions();
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();

        binding.optionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(binding.optionA.getTag().toString());
            }
        });
        binding.optionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(binding.optionB.getTag().toString());
            }
        });
        binding.optionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(binding.optionC.getTag().toString());
            }
        });
        binding.optionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(binding.optionD.getTag().toString());
            }
        });

    }

    private void showNextQuestion() {
        binding.optionA.setTextColor(textColorDefaultRb);
        binding.optionB.setTextColor(textColorDefaultRb);
        binding.optionC.setTextColor(textColorDefaultRb);
        binding.optionB.setTextColor(textColorDefaultRb);

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            binding.textViewQuestion.setText(currentQuestion.getQuestion());
            setOption(binding.optionA, "A. ", currentQuestion.getOption1());
            setOption(binding.optionB, "B. ", currentQuestion.getOption2());
            setOption(binding.optionC, "C. ", currentQuestion.getOption3());
            setOption(binding.optionD, "D. ", currentQuestion.getOption4());

            questionCounter++;
            binding.textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }

    private void setOption(TextView optionView, String suffix, String option) {
        if (option==null) {
            optionView.setVisibility(View.GONE);
        } else {
            optionView.setVisibility(View.VISIBLE);
        }
        optionView.setText(suffix + option);
        optionView.setTag(option);
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                skipAndShowNextQuestion();
            }
        }.start();
    }

    private void skipAndShowNextQuestion() {
        score -= 5;
        showNextQuestion();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d", seconds);

        binding.textViewCountdown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            binding.textViewCountdown.setTextColor(Color.RED);
        } else {
            binding.textViewCountdown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer(String answerNr) {
        answered = true;

        countDownTimer.cancel();


        if (answerNr.equals(currentQuestion.getAnswerNr())) {
            score += 20;
            binding.textViewScore.setText("Score: " + score);
        } else {
            score -= 10;
        }
        showNextQuestion();
//        showSolution();

    }

    private void showSolution() {
        binding.optionA.setTextColor(Color.RED);
        binding.optionB.setTextColor(Color.RED);
        binding.optionC.setTextColor(Color.RED);
        binding.optionD.setTextColor(Color.RED);

//        switch (currentQuestion.getAnswerNr()) {
//            case 1:
//                binding.optionA.setTextColor(Color.GREEN);
//                textViewQuestion.setText("Answer 1 is correct");
//                break;
//            case 2:
//                binding.optionB.setTextColor(Color.GREEN);
//                textViewQuestion.setText("Answer 2 is correct");
//                break;
//            case 3:
//                binding.optionC.setTextColor(Color.GREEN);
//                textViewQuestion.setText("Answer 3 is correct");
//                break;
//        }

//        if (questionCounter < questionCountTotal) {
//            buttonConfirmNext.setText("Next");
//        } else {
//            buttonConfirmNext.setText("Finish");
//        }
    }

    private void finishQuiz() {
//        level();
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        resultIntent.putExtra(EXTRA_SCORE, score);
        resultIntent.putExtra(FUll_SCORE, questionCountTotal * 20);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void level() {
        if (score == 1) {
            Toast.makeText(this, "Your next level Predicted is Easy", Toast.LENGTH_SHORT).show();
        } else if (score == 2) {
            Toast.makeText(this, "Your next level Predicted is Easy", Toast.LENGTH_SHORT).show();
        } else if (score == 3) {
            Toast.makeText(this, "Your next level Predicted is Medium", Toast.LENGTH_SHORT).show();
        } else if (score == 4) {
            Toast.makeText(this, "Your next level Predicted is Medium", Toast.LENGTH_SHORT).show();
        } else if (score == 5) {
            Toast.makeText(this, "Your next level Predicted is Hard", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}