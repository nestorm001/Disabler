package com.github.nestorm001.disabler

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import java.io.DataOutputStream
import java.lang.Exception
import java.lang.IllegalArgumentException


/**
 * Created on 2017/4/14.
 * By nesto
 */

// regard default as enabled
fun PackageManager.isEnabled(packageName: String): Boolean {
    try {
        val setting = getApplicationEnabledSetting(packageName)
        return setting == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ||
                setting == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
    } catch (e: IllegalArgumentException) {
        return false
    }
}

fun PackageManager.isDisabled(packageName: String): Boolean {
    try {
        val setting = getApplicationEnabledSetting(packageName)
        return setting == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                || setting == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
    } catch (e: IllegalArgumentException) {
        return false
    }
}

fun enablePackage(packageName: String) {
    exec("pm enable " + packageName)
}

fun disablePackage(packageName: String) {
    exec("pm disable " + packageName)
}

fun hidePackage(packageName: String) {
    exec("pm hide " + packageName)
}

fun unhidePackage(packageName: String) {
    exec("pm unhide " + packageName)
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

fun openApp(context: Context, packageName: String) {
    val manager = context.packageManager
    try {
        val i = manager.getLaunchIntentForPackage(packageName)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        context.startActivity(i)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}