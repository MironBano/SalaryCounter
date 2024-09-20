package com.bano.mynewproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.bano.mynewproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var lastBackPressedTime: Long = 0
    private val exitDelay: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBackPressedCallback()
        binding.button.setOnClickListener(buttonClickListener())
    }

    private fun buttonClickListener() = View.OnClickListener {
        if (binding.editTextNumber.text.isNotEmpty() && binding.editTextNumber2.text.isNotEmpty() && binding.editTextNumber3.text.isNotEmpty()) {
            val amount = binding.editTextNumber.text.toString().toInt()
            val hoursInDay = binding.editTextNumber2.text.toString().toInt()
            val totalDays = binding.editTextNumber3.text.toString().toInt()
            val salaryInformation = SalaryInformation(amount, hoursInDay, totalDays)

            val intent = Intent(this@MainActivity, CounterActivity::class.java)
            intent.putExtra("salaryInformation", salaryInformation)
            startActivity(intent)
        } else {
            Toast.makeText(this, R.string.warningMissingInformation, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this) {
            if (System.currentTimeMillis() - lastBackPressedTime < exitDelay) {
                finish()
            } else {
                lastBackPressedTime = System.currentTimeMillis()
                Toast.makeText(
                    this@MainActivity, R.string.warningClosingActivity, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}