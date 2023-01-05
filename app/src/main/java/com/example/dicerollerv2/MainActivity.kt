package com.example.dicerollerv2

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.*
import kotlin.math.sqrt
import androidx.recyclerview.widget.RecyclerView
import com.example.dicerollerv2.chat.ChatBox
import com.example.dicerollerv2.chat.ChatLog
import com.example.dicerollerv2.chat.ChatLogDto
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    lateinit var diceImage: ImageView
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var modificator = 0
    private val db = Firebase.firestore
    private val dice = Dice()
    lateinit var appId: String

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<ChatLog>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Installations", "Installation ID: " + task.result)
                appId = task.result
                getUserData(appId)
            } else {
                Log.e("Installations", "Unable to get Installation ID")
                displayError("Unable to get Installation ID")
            }
        }

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
            var rollValue = rollDice()
            populateRollList(rollValue, modificator)
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

        newRecyclerView = findViewById(R.id.chatBox)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newArrayList = arrayListOf()

        val clearData: Button = findViewById(R.id.btnClear)
        clearData.setOnClickListener {
            deleteUserData(appId)
        }
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

        //val rollButton: Button = findViewById(R.id.btRandom)
        //val numberD: TextView = findViewById(R.id.textNumberRandom)

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
        //val randomInt = Random.nextInt(6) + 1
        val randomInt = dice.roll6()

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

    private fun populateRollList(rollValue: Int, modificator: Int?) {

        val date = Calendar.getInstance().time

        val data: ChatLogDto = ChatLogDto(rollValue, modificator, date)

        val finalValue = rollValue + modificator!!


        val log = ChatLog("$date \n You rolled: $rollValue with modifier: $modificator \n Final value: $finalValue",date, rollValue, modificator)
        newArrayList.add(0,log)

        newRecyclerView.adapter = ChatBox(newArrayList)

        db.collection("diceRolls").document(appId).collection("rolls").document()
            .set(data, SetOptions.merge())
            .addOnSuccessListener { Log.d(TAG, "Added to database") }
            .addOnFailureListener {e -> Log.w(TAG, "Error writing to database", e)}
    }

    private fun getUserData(appId: String)
    {
        db.collection("diceRolls").document(appId).collection("rolls")
            .orderBy("rollDate")
            .get()
            .addOnSuccessListener { result ->
                for (document in result)
                {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    var rollValue = (document.data["rollValue"] as Long).toInt()
                    var modificatorValue = (document.data["modificatorValue"] as Long).toInt()
                    var rollDate = document.getTimestamp("rollDate")?.toDate()

                    var finalValue = rollValue + modificatorValue

                    val log = ChatLog("$rollDate \n You rolled: $rollValue with modifier: $modificator \n Final value: $finalValue",
                        rollDate,
                        rollValue,
                        modificatorValue
                    )
                    newArrayList.add(0,log)
                    newRecyclerView.adapter = ChatBox(newArrayList)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents", exception)
            }
        return
    }

    private fun deleteUserData(appId: String)
    {
        db.collection("diceRolls").document(appId).collection("rolls")
            .get()
            .addOnSuccessListener { result ->
                for (document in result)
                {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    db.collection("diceRolls").document(appId).collection("rolls").document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Document deleted.")
                            newArrayList.clear()
                            newRecyclerView.adapter = ChatBox(newArrayList)
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
                }
            }
    }

    private val positiveButtonClick = { _: DialogInterface, _: Int ->
        closeApp()
        Toast.makeText(applicationContext,
        "Ok", Toast.LENGTH_SHORT).show()
    }

    private fun displayError(message: String)
    {
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Alert")
            setMessage(message)
            setPositiveButton("Close", DialogInterface.OnClickListener(function = positiveButtonClick))
            show()
        }
    }

    private fun closeApp(): (DialogInterface, Int) -> Unit {
        this@MainActivity.finish()
        exitProcess(0)
    }


}