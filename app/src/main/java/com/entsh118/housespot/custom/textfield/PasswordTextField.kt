package com.entsh118.housespot.custom.textfield

import android.content.Context
import android.util.AttributeSet
import com.entsh118.housespot.R
import com.entsh118.housespot.custom.CustomTextField

class PasswordTextField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CustomTextField(context, attrs) {

    init {
        background = context.getDrawable(R.drawable.rounded_edittext)
        setPadding(16, 16, 16, 16)
        compoundDrawablePadding = 16
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0)
        hint = "Kata Sandi"
        inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun validate(s: CharSequence) {
        if (s.length < 5) {
            error = "Kata Sandi tidak boleh kurang dari 5 karakter"
        }
    }
}
