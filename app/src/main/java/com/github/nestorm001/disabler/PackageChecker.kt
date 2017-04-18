package com.github.nestorm001.disabler

import android.content.Context
import android.content.Intent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception

fun Context.enablePackage(packageName: String, lambda: (Int) -> (Unit)?) {
    exec("pm enable " + packageName, lambda)
}

fun Context.disablePackage(packageName: String, lambda: (Int) -> (Unit)?) {
    exec("pm disable " + packageName, lambda)
}

fun Context.exec(cmd: String, lambda: (Int) -> (Unit)?) {
    doAsync {
        val process = Runtime.getRuntime().exec(arrayOf("su", "-c", cmd))
        uiThread { lambda.invoke(process.waitFor()) }
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