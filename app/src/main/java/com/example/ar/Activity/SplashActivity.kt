package com.example.ar.Activity

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import com.example.ar.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding.textView.alpha = 0f

        val fadeInAnimator = ObjectAnimator.ofFloat(binding.textView, "alpha", 0f, 1f)
        fadeInAnimator.duration = 3500
        fadeInAnimator.interpolator = AccelerateDecelerateInterpolator()
        fadeInAnimator.start()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, select::class.java))
            finish()
        }, 4000) // 3 seconds

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}