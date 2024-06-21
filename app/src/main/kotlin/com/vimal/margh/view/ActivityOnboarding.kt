package com.vimal.margh.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vimal.margh.R
import com.vimal.margh.databinding.ActivityOnboradingBinding
import com.vimal.margh.util.SharedPrefer
import com.vimal.margh.util.Utils

class ActivityOnboarding : AppCompatActivity() {

    private val sharedPrefer: SharedPrefer by lazy { SharedPrefer(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOnboradingBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.getRoot())

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        try {
            if (sharedPrefer.getOnBoarding()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        } catch (e: Exception) {
            Utils.getErrors(e)
        }

    }
}