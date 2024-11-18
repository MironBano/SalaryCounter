package com.bano.mynewproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.bano.mynewproject.CounterList.SavedCountersActivity
import com.bano.mynewproject.databinding.ActivityMainBinding

class SalaryCounterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var lastBackPressedTime: Long = 0
    private val exitDelay: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackPressedCallback()
        binding.startButton.setOnClickListener(buttonClickListener())

        setSupportActionBar(binding.toolbarMain)
        with(supportActionBar) {
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back)
            this?.setDisplayShowHomeEnabled(true)
        }

    }

    private fun buttonClickListener() = View.OnClickListener {
        if (binding.editText1.text.isNotEmpty() && binding.editText2.text.isNotEmpty() && binding.editText3.text.isNotEmpty()) {
            val amount = binding.editText1.text.toString().toInt()
            val hoursInDay = binding.editText2.text.toString().toInt()
            val totalDays = binding.editText3.text.toString().toInt()
            val salaryInformation = SalaryInformation(amount, hoursInDay, totalDays)

            val intent = Intent(this@SalaryCounterActivity, CounterActivity::class.java)
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
                    this@SalaryCounterActivity, R.string.warningClosingActivity, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.saved_counters -> {
                val intent = Intent(this@SalaryCounterActivity, SavedCountersActivity::class.java)
                startActivity(intent)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}