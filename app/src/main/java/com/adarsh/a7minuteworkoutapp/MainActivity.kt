package com.adarsh.a7minuteworkoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.adarsh.a7minuteworkoutapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

//        val flStartButton:FrameLayout = findViewById(R.id.flstart)
        binding?.flstart?.setOnClickListener {

            val intent = Intent(this, ExcerciseActivity::class.java)
            startActivity(intent)
        }

        binding?.flBMI?.setOnClickListener {

            val intent = Intent(this, BMIActivity::class.java)
            startActivity(intent)
        }

        binding?.flHistory?.setOnClickListener {

            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}