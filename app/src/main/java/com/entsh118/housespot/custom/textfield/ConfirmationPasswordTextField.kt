package com.entsh118.housespot.custom.textfield

import android.content.Context
import android.util.AttributeSet
import com.entsh118.housespot.R
import com.entsh118.housespot.custom.CustomTextField

class ConfirmationPasswordTextField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CustomTextField(context, attrs) {

    private var originalPassword: String = ""

    init {
        background = context.getDrawable(R.drawable.rounded_edittext)
        setPadding(16, 16, 16, 16)
        compoundDrawablePadding = 16
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_password, 0, 0, 0)
        hint = "Konfirmasi Kata Sandi"
    }

    override fun validate(s: CharSequence) {
        if (s.toString() != originalPassword) {
            error = "Password tidak cocok"
        }
    }

    fun setOriginalPassword(password: String) {
        originalPassword = password
    }
}
