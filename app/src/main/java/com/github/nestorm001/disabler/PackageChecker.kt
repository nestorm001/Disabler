package com.github.nestorm001.disabler

import android.content.Context
import android.content.Intent
import java.io.DataOutputStream
import java.lang.Exception

fun enablePackage(packageName: String) {
    exec("pm enable " + packageName)
}

fun disablePackage(packageName: String) {
    exec("pm disable " + packageName)
}

fun exec(cmd: String) {
    try {
        val process = Runtime.getRuntime().exec("su")
        val request = DataOutputStream(process.outputStream)
        request.write(cmd.toByteArray())
        request.flush()
        request.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.openApp(packageName: String) {
    val manager = this.packageManager
    try {
        val i = manager.getLaunchIntentForPackage(packageName)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        this.startActivity(i)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}