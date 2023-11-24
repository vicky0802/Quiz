package com.example.quiz

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz.databinding.ActivityQuizBinding
import java.util.Collections
import java.util.Locale

class QuizActivity : AppCompatActivity() {
    var binding: ActivityQuizBinding? = null
    private var textColorDefaultRb: ColorStateList? = null
    private var textColorDefaultCd: ColorStateList? = null
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0
    private var questionList: ArrayList<Question?>? = null
    private var questionCounter = 0
    private var questionCountTotal = 0
    private var currentQuestion: Question? = null
    private var score = 0
    private var answered = false
    private var backPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        textColorDefaultRb = binding?.optionA?.textColors
        textColorDefaultCd = binding?.textViewCountdown?.textColors
        val dbHelper = QuizDbHelper(this)
        dbHelper.createDatabase()
        questionList = dbHelper.allQuestions as ArrayList<Question?>?
        questionCountTotal = questionList?.size ?: 0
        Collections.shuffle(questionList)
        showNextQuestion()
        binding?.optionA?.setOnClickListener { checkAnswer(binding?.optionA?.tag.toString()) }
        binding?.optionB?.setOnClickListener { checkAnswer(binding?.optionB?.tag.toString()) }
        binding?.optionC?.setOnClickListener { checkAnswer(binding?.optionC?.tag.toString()) }
        binding?.optionD?.setOnClickListener { checkAnswer(binding?.optionD?.tag.toString()) }
    }

    private fun showNextQuestion() {
        binding?.optionA?.setTextColor(textColorDefaultRb)
        binding?.optionB?.setTextColor(textColorDefaultRb)
        binding?.optionC?.setTextColor(textColorDefaultRb)
        binding?.optionB?.setTextColor(textColorDefaultRb)
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList?.get(questionCounter)
            binding?.textViewQuestion?.text = currentQuestion?.question
            setOption(binding?.optionA, "A. ", currentQuestion?.option1)
            setOption(binding?.optionB, "B. ", currentQuestion?.option2)
            setOption(binding?.optionC, "C. ", currentQuestion?.option3)
            setOption(binding?.optionD, "D. ", currentQuestion?.option4)
            questionCounter++
            binding?.textViewQuestionCount?.text = "Question: $questionCounter/$questionCountTotal"
            answered = false
            timeLeftInMillis = COUNTDOWN_IN_MILLIS
            startCountDown()
        } else {
            finishQuiz()
        }
    }

    private fun setOption(optionView: TextView?, suffix: String, option: String?) {
        if (option == null) {
            optionView?.visibility = View.GONE
        } else {
            optionView?.visibility = View.VISIBLE
        }
        optionView?.text = suffix + option
        optionView?.tag = option
    }

    private fun startCountDown() {
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                timeLeftInMillis = 0
                updateCountDownText()
                skipAndShowNextQuestion()
            }
        }.start()
    }

    private fun skipAndShowNextQuestion() {
        score -= 5
        showNextQuestion()
    }

    private fun updateCountDownText() {
        val minutes = (timeLeftInMillis / 1000).toInt() / 60
        val seconds = (timeLeftInMillis / 1000).toInt() % 60
        val timeFormatted = String.format(Locale.getDefault(), "%02d", seconds)
        binding?.textViewCountdown?.text = timeFormatted
        if (timeLeftInMillis < 10000) {
            binding?.textViewCountdown?.setTextColor(Color.RED)
        } else {
            binding?.textViewCountdown?.setTextColor(textColorDefaultCd)
        }
    }

    private fun checkAnswer(answerNr: String) {
        answered = true
        countDownTimer?.cancel()
        if (answerNr == currentQuestion?.answerNr) {
            score += 20
            binding?.textViewScore?.text = "Score: $score"
        } else {
            score -= 10
        }
        showNextQuestion()
        //        showSolution();
    }

    private fun showSolution() {
        binding?.optionA?.setTextColor(Color.RED)
        binding?.optionB?.setTextColor(Color.RED)
        binding?.optionC?.setTextColor(Color.RED)
        binding?.optionD?.setTextColor(Color.RED)

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

    private fun finishQuiz() {
//        level();
        val resultIntent = Intent(this, ResultActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        resultIntent.putExtra(EXTRA_SCORE, score)
        resultIntent.putExtra(FUll_SCORE, questionCountTotal * 20)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun level() {
        if (score == 1) {
            Toast.makeText(this, "Your next level Predicted is Easy", Toast.LENGTH_SHORT).show()
        } else if (score == 2) {
            Toast.makeText(this, "Your next level Predicted is Easy", Toast.LENGTH_SHORT).show()
        } else if (score == 3) {
            Toast.makeText(this, "Your next level Predicted is Medium", Toast.LENGTH_SHORT).show()
        } else if (score == 4) {
            Toast.makeText(this, "Your next level Predicted is Medium", Toast.LENGTH_SHORT).show()
        } else if (score == 5) {
            Toast.makeText(this, "Your next level Predicted is Hard", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz()
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null) {
            countDownTimer?.cancel()
        }
    }


    companion object {
        const val EXTRA_SCORE = "extraScore"
        const val FUll_SCORE = "fullScore"
        private const val COUNTDOWN_IN_MILLIS: Long = 30000
    }
}