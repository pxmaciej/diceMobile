package com.example.dicerollerv2

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import java.util.*
import kotlin.math.sqrt
import kotlin.random.Random

class MainActivity3 : AppCompatActivity() {

    lateinit var diceImage: ImageView
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        Objects.requireNonNull(sensorManager)!!
            .registerListener(
                sensorListener, sensorManager!!
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
            )

        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

        val rollButton: Button = findViewById(R.id.btnRoll)
        rollButton.setOnClickListener {
            rollDice()
        }

        val nextDice: Button = findViewById(R.id.btnNextDice)
        nextDice.setOnClickListener {
            startActivity(Intent(this, MainActivity4::class.java))
        }

        val backDice: Button = findViewById(R.id.btnBackDice)
        backDice.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }


        diceImage = findViewById(R.id.diceImage)
    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            // Fetching x,y,z values
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            // Getting current accelerations
            // with the help of fetched x,y,z values
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            // Display a Toast message if
            if (acceleration > 3) {
                rollDice()
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }

    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }

    private fun rollDice() {
        val randomInt = Random.nextInt(10) + 1
        val drawableResource = when (randomInt) {
            1 -> R.drawable.d10_1
            2 -> R.drawable.d10_2
            3 -> R.drawable.d10_3
            4 -> R.drawable.d10_4
            5 -> R.drawable.d10_5
            6 -> R.drawable.d10_6
            7 -> R.drawable.d10_7
            8 -> R.drawable.d10_8
            9 -> R.drawable.d10_9
            else -> R.drawable.dice10_10
        }

        diceImage.setImageResource(drawableResource)
    }
}