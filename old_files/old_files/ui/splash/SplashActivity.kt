package com.jomar.senhorpintor.ui.splash

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.makeItFullScreen
import com.jomar.senhorpintor.presentation.splash.SplashViewModel
import com.jomar.senhorpintor.ui.home.HomeActivity
import com.jomar.senhorpintor.ui.onboarding.IntroActivity
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeItFullScreen()
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({ checkFirstRunning() }, 2000)
    }

    private fun checkFirstRunning() {
        if (viewModel.isFirstRunning()) {
            startActivity<IntroActivity>()
        } else {
            startActivity<HomeActivity>()
        }
        finish()
    }
}


