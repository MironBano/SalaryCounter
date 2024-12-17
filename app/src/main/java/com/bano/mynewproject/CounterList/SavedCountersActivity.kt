package com.bano.mynewproject.CounterList

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bano.mynewproject.R
import com.bano.mynewproject.SalaryInformation
import com.bano.mynewproject.databinding.ActivitySavedCountersBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.TimeUnit

class SavedCountersActivity : AppCompatActivity(), AddCounterFragment.OnCounterAddedListener {

    private lateinit var binding: ActivitySavedCountersBinding
    private lateinit var counterJob: Job
    private val counters = mutableListOf<CounterListItem>()
    private val bornTimesOfCounters = mutableListOf<BornTime>()
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
        binding.countersLV.adapter = CounterListAdapter(counters)

        //TODO("Implement getting info from db/files")
        //TODO("Implement saving state of counters")
        counterJob = lifecycleScope.launch {
            for (counter in counters) {
                val bornTime = bornTimesOfCounters.find { it.counterName == counter.counterName }
                bornTime?.let { startCounter(counter, it) }
            }
        }
    }

    private fun startCounter(counterListItem: CounterListItem, bornTime: BornTime) {
        val amountUnit: Double =
            with(counterListItem.salaryInformation) {
                amount.toDouble() / hoursInDay.toDouble() / days.toDouble() / 3600
            }.toDouble()
        lifecycleScope.launch {
            var counterIntState = calculateAmountUnitFromBirth(counterListItem, bornTime)
            while (true) {
                counterIntState += amountUnit
                counterListItem.counterState = "%.2f".format(counterIntState) + "₽"
                delay(1000)
                (binding.countersLV.adapter as CounterListAdapter).notifyDataSetChanged()
            }
        }
    }

    private fun calculateAmountUnitFromBirth(
        counterListItem: CounterListItem,
        bornTime: BornTime
    ): Double {
        val amountUnit: Double =
            with(counterListItem.salaryInformation) {
                amount.toDouble() / hoursInDay.toDouble() / days.toDouble() / 3600
            }.toDouble()
        val bornAmountUnit: Double =
            TimeUnit.MILLISECONDS.toSeconds(Date().time - bornTime.dateTime.time)
                .toDouble() * amountUnit

        return bornAmountUnit
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

    override fun onCounterAdded(
        salaryInformation: SalaryInformation,
        counter: CounterListItem,
        bornTime: BornTime
    ) {
        counters.add(counter)
        bornTimesOfCounters.add(bornTime)
        startCounter(counter, bornTime)
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

