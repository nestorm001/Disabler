package com.github.nestorm001.disabler

import android.content.Context
import android.content.Intent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.DataOutputStream
import java.lang.Exception

@FunctionalInterface
interface Action {
    fun execResult(resultValue: Int)
}

fun Action(lambda: (Int) -> (Unit)): Action = object : Action {
    override fun execResult(resultValue: Int) = lambda.invoke(resultValue)
}

fun Context.enablePackage(packageName: String, action: Action? = null) {
    exec("pm enable " + packageName, action)
}

fun Context.disablePackage(packageName: String, action: Action? = null) {
    exec("pm disable " + packageName, action)
}

fun Context.exec(cmd: String, action: Action? = null) {
    doAsync {
        try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", cmd))
            val request = DataOutputStream(process.outputStream)
            request.flush()
            process.waitFor()
            uiThread {
                action?.execResult(process.exitValue())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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