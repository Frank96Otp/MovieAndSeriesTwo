package com.example.moviesandseriestwo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        Log.i("Splash", "Entro por el SplasActivity")
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        screenSplash.setKeepOnScreenCondition{false}
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}