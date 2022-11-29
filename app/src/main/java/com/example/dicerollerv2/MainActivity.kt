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
import android.widget.Toast
import java.util.*
import kotlin.math.sqrt
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var diceImage: ImageView
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var modificator = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            startActivity(Intent(this, MainActivity2::class.java))
        }

        val backDice: Button = findViewById(R.id.btnBackDice)
        backDice.setOnClickListener {
            startActivity(Intent(this, MainActivity4::class.java))
        }

        val negativeMod5: Button = findViewById(R.id.btnMod_5)
        negativeMod5.setOnClickListener {
            modificator = -5
            showToast(modificator)
        }

        val negativeMod1: Button = findViewById(R.id.btnMod_1)
        negativeMod1.setOnClickListener {
            modificator = -1
            showToast(modificator)
        }

        val positivelyMod1: Button = findViewById(R.id.btnMod1)
        positivelyMod1.setOnClickListener {
            modificator = 1
            showToast(modificator)
        }

        val positivelyMod5: Button = findViewById(R.id.btnMod5)
        positivelyMod5.setOnClickListener {
            modificator = 5
            showToast(modificator)
        }

        diceImage = findViewById(R.id.diceImage)
    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration

            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            // Display a Toast message if
            // acceleration value is over 12
            if (acceleration > 2) {
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

    private fun rollDice(): Int {
        val randomInt = Random.nextInt(6) + 1
        val drawableResource = when (randomInt) {
            1 -> R.drawable.d6_1
            2 -> R.drawable.d6_2
            3 -> R.drawable.d6_3
            4 -> R.drawable.d6_4
            5 -> R.drawable.d6_5
            else -> R.drawable.d6_6
        }

        diceImage.setImageResource(drawableResource)

        return randomInt
    }

    private fun showToast(text: Int) {

        val toast = Toast.makeText(this, "$text", Toast.LENGTH_SHORT)

        toast.show()
    }
}