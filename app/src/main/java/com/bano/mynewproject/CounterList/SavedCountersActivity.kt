package com.bano.mynewproject.CounterList

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Addr
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bano.mynewproject.R
import com.bano.mynewproject.SalaryCounterActivity
import com.bano.mynewproject.SalaryInformation
import com.bano.mynewproject.databinding.ActivitySavedCountersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SavedCountersActivity : AppCompatActivity(), AddCounterFragment.OnCounterAddedListener {

    private lateinit var binding: ActivitySavedCountersBinding
    private lateinit var counterJob: Job
    private val counters = mutableListOf<CounterListItem>()
    private var lastBackPressedTime: Long = 0
    private val exitDelay: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedCountersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackPressedCallback()
        setSupportActionBar(binding.toolbarSC)
        with(supportActionBar) {
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back)
            this?.setDisplayShowHomeEnabled(true)
        }

        //TODO("Implement getting info from db/files")
        //TODO("Implement saving state of counters")
        //TODO("Implement adding counters by user")

        val testSalaryInfo = SalaryInformation(60000, 12, 22)
        counters += CounterListItem("Salary1", testSalaryInfo.amount.toString())
        counters += CounterListItem("Salary2", testSalaryInfo.amount.toString())
        counters += CounterListItem("Salary3", testSalaryInfo.amount.toString())

        binding.countersLV.adapter = CounterListAdapter(counters)

        counterJob = CoroutineScope(Dispatchers.Main).launch {
            for (counter in counters) startCounter(testSalaryInfo, counter)
        }

    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun startCounter(
        salaryInfo: SalaryInformation,
        counterListItem: CounterListItem
    ) {
        val amountUnit: Double =
            salaryInfo.amount.toDouble() / salaryInfo.hoursInDay.toDouble() / salaryInfo.days.toDouble() / 3600
        lifecycleScope.launch {
            var counter = 0.0
            while (true) {
                counter += amountUnit
                counterListItem.counterState = String.format("%.2f", counter) + "₽"
                delay(1000)
                (binding.countersLV.adapter as CounterListAdapter).notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.saved_counters_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add_counter -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.addFragmentContainer, AddCounterFragment())
                    .addToBackStack(null)
                    .commit()
                binding.addFragmentContainer.setBackgroundColor(getColor(R.color.black))
                true
            }

            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCounterAdded(salaryInfo: SalaryInformation, counter: CounterListItem) {
        counters.add(counter)
        startCounter(salaryInfo, counter)
        (binding.countersLV.adapter as CounterListAdapter).notifyDataSetChanged()
        binding.addFragmentContainer.setBackgroundColor(getColor(R.color.transparent))
    }

    private fun setupBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
                binding.addFragmentContainer.setBackgroundColor(getColor(R.color.transparent))
            } else if (System.currentTimeMillis() - lastBackPressedTime < exitDelay) {
                finish()
            } else {
                lastBackPressedTime = System.currentTimeMillis()
                Toast.makeText(
                    this@SavedCountersActivity,
                    R.string.warningClosingActivity,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

