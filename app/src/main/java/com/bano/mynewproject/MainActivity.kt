package com.bano.mynewproject

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        binding.startButton.setOnClickListener(buttonClickListener())

        setSupportActionBar(binding.toolbarMain)

    }

    private fun buttonClickListener() = View.OnClickListener {
        if (binding.editText1.text.isNotEmpty() && binding.editText2.text.isNotEmpty() && binding.editText3.text.isNotEmpty()) {
            val amount = binding.editText1.text.toString().toInt()
            val hoursInDay = binding.editText2.text.toString().toInt()
            val totalDays = binding.editText3.text.toString().toInt()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.countersMenuMain -> {
                val intent = Intent(this@MainActivity, SavedCountersActivity::class.java)
                startActivity(intent)
            }

            R.id.exitMenuMain -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}