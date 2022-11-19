package com.example.dicerollerv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton: Button = findViewById(R.id.btRandom)
        val numberD: TextView = findViewById(R.id.textNumberRandom)

        rollButton.setOnClickListener{
            val tmp = rollD20()
            numberD.text = "$tmp"

        }


    }

    private fun rollD20(): Int {
        return Random.nextInt(19) + 1
    }
}