package com.bano.mynewproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*


class CounterActivity : AppCompatActivity() {

    private lateinit var counterTV: TextView
    private lateinit var stopBTN: Button
    private lateinit var counterJob: Job
    private var counter = 0.0
    private var lastBackPressedTime: Long = 0
    private val exitDelay: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter)

        counterTV = findViewById(R.id.counterTV)
        stopBTN = findViewById(R.id.stopBTN)

        val salaryInformation: SalaryInformation? = intent.getParcelableExtra("salaryInformation")

        counterTV.text = salaryInformation!!.amount.toString()

        counterJob = CoroutineScope(Dispatchers.Main).launch { startCounter(salaryInformation) }

        stopBTN.setOnClickListener {
            try {
                if (counterJob.isActive) counterJob.cancel()
            } catch (_: UninitializedPropertyAccessException) {
            }

            startActivity(
                Intent(this, MainActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    }

    private suspend fun startCounter(salaryInfo: SalaryInformation) {
        val amount = salaryInfo.amount.toDouble()
        val hours = salaryInfo.hoursInDay.toDouble()
        val days = salaryInfo.days.toDouble()
        val amountUnit: Double = amount / days / hours / 3600
        coroutineScope {
            launch {
                while (true) {
                    counter += amountUnit
                    counterTV.text = String.format("%.2f", counter)
                    delay(1000)
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (System.currentTimeMillis() - lastBackPressedTime < exitDelay) {
            try {
                if (counterJob.isActive) counterJob.cancel()
            } catch (_: UninitializedPropertyAccessException) {
            }
            startActivity(
                Intent(this, MainActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        } else {
            lastBackPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "Нажмите ещё раз, чтобы выйти", Toast.LENGTH_SHORT).show()
        }
    }


}