package com.jomar.senhorpintor.ui.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jomar.senhorpintor.R

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        this.supportActionBar!!.hide()
    }
}
