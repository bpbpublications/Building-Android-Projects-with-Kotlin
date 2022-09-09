package com.bpb.android.clips.ui.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

fun AppCompatTextView.setTextOrHide(value: String? = null) {
    if (!value.isNullOrBlank()) {
        text = value
        isVisible = true
    } else {
        isVisible = false
    }
}

fun ShapeableImageView.loadCenterCropImageFromUrl(imageUrl: String?) {
    Glide.with(this)
        .load(imageUrl)
        .centerCrop()
        .into(this)
}

fun <T> Class<in T>.logError(message: String) {
    Log.e(this::class.java.name, message)
}

fun Any.logError(message: String) {
    Log.e(this::class.java.name, message)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Long.formatNumberAsReadableFormat(): String {
    return this.formatNumberAsNumberFormat()
}

fun Long.formatNumberAsNumberFormat(): String {

    val value = this
    if (value < 0) return 0L.formatNumberAsNumberFormat()
    if (value < 1000) return value.toString()
    val entry = Constants.POST_FIX.floorEntry(value)

    entry?.run {
        val divideBy = entry.key
        val suffix = entry.value
        val truncated = value / (divideBy / 10)
        val hasDecimal =
            truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
        return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix

    }
    return 0L.formatNumberAsNumberFormat()
}