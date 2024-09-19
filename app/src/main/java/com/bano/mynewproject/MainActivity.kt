package com.bano.mynewproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bano.mynewproject.databinding.ActivityMainBinding
import kotlinx.coroutines.channels.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var lastBackPressedTime: Long = 0
    private val exitDelay: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener(buttonClickListener())
    }

    private fun buttonClickListener() = View.OnClickListener {
        if (binding.editTextNumber.text.isNotEmpty() &&
            binding.editTextNumber2.text.isNotEmpty() &&
            binding.editTextNumber3.text.isNotEmpty()) {

            val amount = binding.editTextNumber.text.toString().toInt()
            val hoursInDay = binding.editTextNumber2.text.toString().toInt()
            val totalDays = binding.editTextNumber3.text.toString().toInt()
            val salaryInformation = SalaryInformation(amount, hoursInDay, totalDays)

            val intent = Intent(this, CounterActivity::class.java)
            intent.putExtra("salaryInformation", salaryInformation)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - lastBackPressedTime < exitDelay) {
            super.onBackPressed()
        } else {
            lastBackPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "Нажмите ещё раз, чтобы выйти", Toast.LENGTH_SHORT).show()
        }
    }
}