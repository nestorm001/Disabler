package com.github.nestorm001.disabler

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.layoutManager = LinearLayoutManager(this)

//        showSystemSwitch()
        setList()
    }

    private fun showSystemSwitch() {
        showType.setOnClickListener {
            count++
            if (count == 10) {
                setList(true)
                count = 0
            } else {
                setList()
            }
        }
    }

    private fun setList(withSystemApp: Boolean = false) {
        doAsync {
            val appList = getList(withSystemApp)
            uiThread {
                list.adapter = PackageAdapter(this@MainActivity, appList)
            }
        }
    }

    private fun getList(withSystemApp: Boolean = false): List<ApplicationInfo> {
        val packageList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                .filter { !it.packageName.contains(packageName, ignoreCase = true) }
        val showList: List<ApplicationInfo>
        if (withSystemApp) {
            showList = packageList
                    .sortedWith(compareBy({ it.flags and ApplicationInfo.FLAG_SYSTEM == 0 },
                            { it.enabled }, { it.packageName }))
        } else {
            showList = packageList
                    .filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 }
                    .sortedWith(compareBy({ it.enabled }, { it.packageName }))
        }
        return showList
    }
}
