package com.example.trackingconverteoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {

    // [START declare_analytics]
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    // [END declare_analytics]

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)


        // FIREBASE
        firebaseAnalytics = Firebase.analytics


        val username = intent.getStringExtra(Constants.USER_NAME)
        tv_name.text = username

        val totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0)
        val correctAnswer = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)
        tv_score.text = "Your score is $correctAnswer out of $totalQuestions"

        //FIREBASE !!!
        firebaseAnalytics.setUserProperty("correct_answer", "$correctAnswer")
        firebaseAnalytics.logEvent("screen") {
            param("screen_name", "Finish")
            param("screen_number", "$totalQuestions")
        }


        btn_finish.setOnClickListener {

            //FIREBASE !!!
            firebaseAnalytics.setUserProperty("reload", "true")

            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}