package com.bano.mynewproject.CounterList

import com.bano.mynewproject.SalaryInformation

data class CounterListItem(
    val counterName: String,
    val salaryInformation: SalaryInformation,
    var counterState: String
)