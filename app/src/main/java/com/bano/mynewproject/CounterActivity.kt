package com.bano.mynewproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import com.bano.mynewproject.databinding.ActivityCounterBinding


class CounterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCounterBinding
    private lateinit var counterJob: Job
    private var counter = 0.0
    private var lastBackPressedTime: Long = 0
    private val exitDelay: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBackPressedCallback()

        val salaryInformation: SalaryInformation? = intent.getParcelableExtra("salaryInformation")

        binding.counterTV.text = salaryInformation!!.amount.toString()
        binding.stopBTN.setOnClickListener {
            startActivity(
                Intent(
                    this, SalaryCounterActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }

        counterJob = CoroutineScope(Dispatchers.Main).launch { startCounter(salaryInformation) }

    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun startCounter(salaryInfo: SalaryInformation) {
        val amount = salaryInfo.amount.toDouble()
        val hours = salaryInfo.hoursInDay.toDouble()
        val days = salaryInfo.days.toDouble()
        val amountUnit: Double = amount / days / hours / 3600
        lifecycleScope.launch {
            while (true) {
                counter += amountUnit
                binding.counterTV.text = String.format("%.2f", counter) + "₽"
                delay(1000)
            }
        }
    }

    private fun setupBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this) {
            if (System.currentTimeMillis() - lastBackPressedTime < exitDelay) {
                startActivity(
                    Intent(
                        this@CounterActivity, SalaryCounterActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
            } else {
                lastBackPressedTime = System.currentTimeMillis()
                Toast.makeText(
                    this@CounterActivity, R.string.warningClosingActivity, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}