package com.entsh118.housespot.custom.textfield

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import com.entsh118.housespot.R
import com.entsh118.housespot.custom.CustomTextField

class EmailTextField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CustomTextField(context, attrs) {

    init {
        background = context.getDrawable(R.drawable.rounded_edittext)
        setPadding(16, 16, 16, 16)
        compoundDrawablePadding = 16
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email, 0, 0, 0)
        hint = "Email"
    }

    override fun validate(s: CharSequence) {
        if (!Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            error = "Email tidak sesuai format"
        }
    }
}
