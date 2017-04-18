package com.github.nestorm001.disabler

import android.content.Context
import android.widget.Toast

/**
 * Created on 2017/4/18.
 * By nesto
 */
var toast: Toast? = null

fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    toast = toast ?: Toast.makeText(this, message, duration)
    toast?.setText(message)
    toast?.duration = duration
    toast?.show()
}