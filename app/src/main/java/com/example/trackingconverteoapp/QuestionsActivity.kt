package com.example.trackingconverteoapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.trackingconverteoapp.R.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_questions.*

class QuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition: Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String? = null

    // [START declare_analytics]
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    // [END declare_analytics]



    override fun onCreate(savedInstanceState: Bundle?) {



        //This call the parent constructor
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_questions)


        // FIREBASE
        firebaseAnalytics = Firebase.analytics


        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        setQuestion()

        // START
        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)

        // FIREBASE
        firebaseAnalytics.logEvent("screen") {
            param("screen_name", "Questions")
            param("screen_number", "$mCurrentPosition")
        }


    }

    override fun onClick(v: View?) {

        when (v?.id) {

            id.tv_option_one -> {

                selectedOptionView(tv_option_one, 1)
            }

            id.tv_option_two -> {

                selectedOptionView(tv_option_two, 2)
            }

            id.tv_option_three -> {

                selectedOptionView(tv_option_three, 3)
            }

            id.tv_option_four -> {

                selectedOptionView(tv_option_four, 4)
            }

            id.btn_submit -> {
                if(mSelectedOptionPosition == 0){
                    mCurrentPosition++

                    when{
                        mCurrentPosition <= mQuestionsList!!.size -> {
                            setQuestion()

                            // FIREBASE
                            firebaseAnalytics.logEvent("screen") {
                                param("screen_name", "Questions")
                                param("screen_number", "$mCurrentPosition")
                            }


                        }else -> {
                            val intent = Intent(this, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, mUserName)
                            intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }else{

                    val question = mQuestionsList?.get(mCurrentPosition -1)
                    if(question!!.correctAnswer != mSelectedOptionPosition) {
                        markButtonDisable()
                        answerView(mSelectedOptionPosition, drawable.wrong_option_border_bg)
                    }else{
                        mCorrectAnswers++
                    }
                    markButtonDisable()
                    answerView(question.correctAnswer, drawable.correct_option_border_bg)

                    if(mCurrentPosition == mQuestionsList!!.size){
                        btn_submit.text = "FINISH"
                    }else{
                        btn_submit.text = "GO TO NEXT QUESTION"

                    }
                    mSelectedOptionPosition = 0
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    private fun markButtonDisable() {
        tv_option_one.isClickable = false
        tv_option_one.alpha = 0.20f
        tv_option_two.isClickable = false
        tv_option_two.alpha = 0.20f
        tv_option_three.isClickable = false
        tv_option_three.alpha = 0.20f
        tv_option_four.isClickable = false
        tv_option_four.alpha = 0.20f


    }

    @SuppressLint("ResourceType")
    private fun answerView(answer: Int, drawableView: Int){
        when(answer){
            1 -> {
                tv_option_one.background = ContextCompat.getDrawable(
                        this, drawableView
                )
                tv_option_one.setTextColor(
                        Color.parseColor(getString(color.white))
                )
                tv_option_one.alpha = 1f
            }
            2 -> {
                tv_option_two.background = ContextCompat.getDrawable(
                        this, drawableView
                )
                tv_option_one.setTextColor(
                        Color.parseColor(getString(color.white))
                )
                tv_option_two.alpha = 1f
            }
            3 -> {
                tv_option_three.background = ContextCompat.getDrawable(
                        this, drawableView
                )
                tv_option_one.setTextColor(
                        Color.parseColor(getString(color.white))
                )
                tv_option_three.alpha = 1f
            }
            4 -> {
                tv_option_four.background = ContextCompat.getDrawable(
                        this, drawableView
                )
                tv_option_one.setTextColor(
                        Color.parseColor(getString(color.white))
                )
                tv_option_four.alpha = 1f
            }
        }
    }

    private fun setQuestion() {

        val question =
                mQuestionsList!![mCurrentPosition - 1] // Getting the question from the list with the help of current position.

        defaultOptionsView()

        if(mCurrentPosition == mQuestionsList!!.size) {
            btn_submit.text = "FINISH"
        }else{
            btn_submit.text = "SUBMIT"
        }


        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition" + "/" + progressBar.getMax()

        tv_question.text = question.question
        iv_image.setImageResource(question.image)
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    @SuppressLint("ResourceType")
    private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {

        defaultOptionsView()

        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(
                Color.parseColor(getString(color.h2))
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
                this@QuestionsActivity,
                drawable.selected_default_option_border_bg
        )
    }


    @SuppressLint("ResourceType")
    private fun defaultOptionsView() {

        val options = ArrayList<TextView>()
        options.add(0, tv_option_one)
        options.add(1, tv_option_two)
        options.add(2, tv_option_three)
        options.add(3, tv_option_four)

        for (option in options) {
            option.isClickable = true
            option.alpha = 1f
            option.setTextColor(
                    Color.parseColor(getString(color.h3))
            )
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                    this@QuestionsActivity,
                    drawable.default_option_border_bg
            )
        }
    }
}