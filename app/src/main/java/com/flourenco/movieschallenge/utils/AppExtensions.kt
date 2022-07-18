package com.flourenco.movieschallenge.utils

import android.content.Context
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Boolean?.get() = this ?: false

fun Int?.get() = this ?: 0

fun Double?.get() = this ?: 0.0

fun String?.get() = this ?: ""

fun View.performHaptic() {
    performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
}

fun View.setOnClickListenerWithHaptic(block: (v: View) -> Unit) {
    if (!isHapticFeedbackEnabled) {
        isHapticFeedbackEnabled = true
    }
    setOnClickListener {
        performHaptic()
        block.invoke(it)
    }
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}