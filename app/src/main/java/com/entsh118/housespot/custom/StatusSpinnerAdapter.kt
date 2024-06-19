package com.entsh118.housespot.custom

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.entsh118.housespot.R

class StatusSpinnerAdapter(context: Context, private val items: List<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, items) {

    private var selectedItemPosition: Int = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        setColorAndStyle(view as TextView, position, position == selectedItemPosition)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        setColorAndStyle(view as TextView, position, position == selectedItemPosition)
        return view
    }

    private fun setColorAndStyle(view: TextView, position: Int, isSelected: Boolean) {
        val color = when (items[position]) {
            "WAITING" -> context.getColor(R.color.waitingColor)
            "ONGOING" -> context.getColor(R.color.ongoingColor)
            "COMPLETED" -> context.getColor(R.color.completedColor)
            "REJECTED" -> context.getColor(R.color.rejectedColor)
            else -> context.getColor(android.R.color.black)
        }
        view.setTextColor(color)
        view.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
    }

    fun setSelectedItemPosition(position: Int) {
        selectedItemPosition = position
        notifyDataSetChanged()
    }
}
