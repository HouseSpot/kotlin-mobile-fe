package com.entsh118.housespot.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.entsh118.housespot.R

class StatusSpinnerAdapter(context: Context, statusArray: Array<String>) :
    ArrayAdapter<String>(context, 0, statusArray) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val textView: TextView = view.findViewById(R.id.spinner_item_text)
        val status = getItem(position)
        textView.text = status
        setStatusColor(textView, status)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)
        val textView: TextView = view.findViewById(R.id.spinner_item_text)
        val status = getItem(position)
        textView.text = status
        setStatusColor(textView, status)
        return view
    }

    private fun setStatusColor(textView: TextView, status: String?) {
        when (status) {
            "Waiting" -> {
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.waitingColor))
            }
            "Ongoing" -> {
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.ongoingColor))
            }
            "Completed" -> {
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.successColor))
            }
            "Rejected" -> {
                textView.setBackgroundColor(ContextCompat.getColor(context, R.color.dangerColor))
            }
        }
    }
}
