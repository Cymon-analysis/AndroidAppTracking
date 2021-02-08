package com.example.trackingconverteoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.*
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {

    // [START declare_analytics]
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    // [END declare_analytics]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //FIREBASE !!!
        firebaseAnalytics = Firebase.analytics

        val parameters = Bundle().apply {
            this.putString("app", "Converteo Tracking Test")
            this.putString("env", "dev")
        }

        firebaseAnalytics.setDefaultEventParameters(parameters)

        firebaseAnalytics.logEvent("screen") {
            param("screen_name", "homepage")
            param("screen_number", "1")
        } //FIREBASE !!!


        btn_start.setOnClickListener {


            if(et_name.text.toString().isEmpty()) {

                //FIREBASE !!!
                firebaseAnalytics.logEvent("click_validated") {
                    param("screen_name", "homepage")
                    param("screen_number", "1")
                    param("click_estate", "wrong")
                }


                Toast.makeText(this, "S'il vous plait, entrez votre nom", Toast.LENGTH_SHORT)
                        .show() // Toast provides feedback to the user.
            }else{


                //FIREBASE !!!
                firebaseAnalytics.logEvent("click_validated") {
                    param("screen_name", "homepage")
                    param("screen_number", "1")
                    param("click_estate", "correct")
                }
                firebaseAnalytics.setUserProperty("user_name", Constants.USER_NAME) // Tout au long de la navigation


                val intent = Intent(this, QuestionsActivity::class.java)
                intent.putExtra(Constants.USER_NAME, et_name.text.toString())
                startActivity(intent)
                finish()
            }
        }
    }
}