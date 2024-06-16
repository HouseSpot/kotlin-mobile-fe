package com.entsh118.housespot.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.entsh118.housespot.R

class CustomImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    init {
        loadImage(R.drawable.ic_vendor)
    }

    fun setImageUrl(url: String) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .into(this)
    }

    override fun setImageResource(resourceId: Int) {
        Glide.with(context)
            .load(resourceId)
            .apply(RequestOptions.circleCropTransform())
            .into(this)
    }

    private fun loadImage(resourceId: Int) {
        Glide.with(context)
            .load(resourceId)
            .apply(RequestOptions.circleCropTransform())
            .into(this)
    }
}
