package com.entsh118.housespot.custom.textfield

import android.content.Context
import android.util.AttributeSet
import com.entsh118.housespot.R
import com.entsh118.housespot.custom.CustomTextField

class NameTextField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CustomTextField(context, attrs) {

    init {
        background = context.getDrawable(R.drawable.rounded_edittext)
        setPadding(16, 16, 16, 16)
        compoundDrawablePadding = 16
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_name, 0, 0, 0)
        hint = "Nama"
    }

    override fun validate(s: CharSequence) {
        if (s.isEmpty()) {
            error = "Nama tidak boleh kosong"
        }
    }
}
