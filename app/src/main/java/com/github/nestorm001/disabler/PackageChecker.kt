package com.github.nestorm001.disabler

import android.content.Context
import android.content.Intent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException
import java.lang.Exception

fun Context.enablePackage(packageName: String, lambda: (Int) -> (Unit)?) {
    exec("pm enable " + packageName, lambda)
}

fun Context.disablePackage(packageName: String, lambda: (Int) -> (Unit)?) {
    exec("pm disable " + packageName, lambda)
}

fun Context.stopPackage(packageName: String, lambda: (Int) -> (Unit)?) {
    exec("am force-stop " + packageName, lambda, needSu = false)
}

fun Context.hidePackage(packageName: String, lambda: (Int) -> (Unit)?) {
    exec("pm hide " + packageName, lambda, needSu = false)
}

fun Context.unhidePackage(packageName: String, lambda: (Int) -> (Unit)?) {
    exec("pm unhide " + packageName, lambda, needSu = false)
}

fun Context.exec(cmd: String, lambda: (Int) -> (Unit)?, needSu: Boolean = true) {
    doAsync {
        var result: Int = 1
        try {
            result = ProcessBuilder(if (needSu) {
                "su"
            } else {
                "sh"
            }, "-c", cmd).start().waitFor()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        uiThread { lambda.invoke(result) }
    }
}

fun Context.openApp(packageName: String) {
    try {
        startActivity(packageManager
                .getLaunchIntentForPackage(packageName)
                .addCategory(Intent.CATEGORY_LAUNCHER))
    } catch (e: Exception) {
        e.printStackTrace()
        toast("Cannot launch this application")
    }
}