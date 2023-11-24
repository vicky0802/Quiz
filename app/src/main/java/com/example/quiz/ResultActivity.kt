package com.example.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiz.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private var textViewHighscore: TextView? = null
    private var highscore = 0
    var binding:ActivityResultBinding?=null
    private var backPressedTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.txtSetScore?.text = "${getString(R.string.your_score_is)} ${intent?.getIntExtra(QuizActivity.EXTRA_SCORE, 0)}/${intent?.getIntExtra(QuizActivity.FUll_SCORE, 0)}"
        binding?.txtStartOver?.setOnClickListener { loadQuiz() }
    }

    private fun loadQuiz() {
        val resultIntent = Intent(this, QuizActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(resultIntent)
    }

    companion object {
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}