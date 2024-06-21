package com.vimal.margh.view

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.vimal.margh.R
import com.vimal.margh.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // window.navigationBarColor = SurfaceColors.SURFACE_0.getColor(this)

        binding.navView.itemIconTintList = null
        binding.navView.setupWithNavController(findNavController(R.id.nav_host_fragment_content_main))

        binding.cvSearch.setOnClickListener {
            val intent = Intent(this, ActivitySearch::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

    }

}


