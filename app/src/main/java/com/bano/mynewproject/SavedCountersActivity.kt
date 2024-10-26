package com.bano.mynewproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bano.mynewproject.databinding.ActivitySavedCountersBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SavedCountersActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedCountersBinding
    private lateinit var counterJob: Job
    private lateinit var counters: MutableList<CounterListItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedCountersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSC)
        with(supportActionBar) {
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back)
            this?.setDisplayShowHomeEnabled(true)
        }

        //TODO("Implement getting info from db/files")
        //TODO("Implement saving state of counters")
        //TODO("Implement adding counters by user")

        val counters = mutableListOf<CounterListItem>()
        val testSalaryInfo = SalaryInformation(60000, 12, 22)
        counters += CounterListItem("Salary1", testSalaryInfo.amount.toString())
        counters += CounterListItem("Salary2", testSalaryInfo.amount.toString())
        counters += CounterListItem("Salary3", testSalaryInfo.amount.toString())

        binding.countersLV.adapter = CounterListAdapter(counters)

        counterJob = CoroutineScope(Dispatchers.Main).launch {
            for (item in counters) startCounter(testSalaryInfo, item)
        }

    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private suspend fun startCounter(
        salaryInfo: SalaryInformation, counterListItem: CounterListItem
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

}