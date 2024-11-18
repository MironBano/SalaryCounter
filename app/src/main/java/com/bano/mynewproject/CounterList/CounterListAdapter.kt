package com.bano.mynewproject.CounterList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bano.mynewproject.R

class CounterListAdapter(private val items: MutableList<CounterListItem>) : BaseAdapter() {
    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(parent?.context)
            .inflate(R.layout.couters_list_item, parent, false)

        val counterName = view.findViewById<TextView>(R.id.counterName)
        val counterState = view.findViewById<TextView>(R.id.counterState)

        val listItem = getItem(position) as CounterListItem
        counterName.text = listItem.counterName
        counterState.text = listItem.counterState
        return view
    }

}