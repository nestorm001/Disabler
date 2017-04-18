package com.github.nestorm001.disabler

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_package_info.view.*

/**
 * Created on 2017/4/16.
 * By nesto
 */
class PackageAdapter(val context: Context, val list: List<ApplicationInfo>)
    : RecyclerView.Adapter<PackageAdapter.ItemViewHolder>() {

    val packageManager: PackageManager = context.packageManager

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val info = list[position]

        holder.convertView.apply {
            packageNameText.text = info.packageName
            applicationNameText.text = packageManager.getApplicationLabel(info)
            icon.setImageDrawable(packageManager.getApplicationIcon(info))
            enableCheckBox.isChecked = !info.enabled
            enableCheckBox.setOnClickListener {
                info.enabled = !enableCheckBox.isChecked
                if (enableCheckBox.isChecked) {
                    disablePackage(info.packageName)
                } else {
                    enablePackage(info.packageName)
                }
            }
            setOnClickListener {
                if (info.enabled) {
                    context.openApp(info.packageName)
                } else {
                    context.toast("To launch the application, you should enable the application first")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemViewHolder {
        val convertView = LayoutInflater.from(context)
                .inflate(R.layout.item_package_info, parent, false)
        return ItemViewHolder(convertView)
    }

    class ItemViewHolder(val convertView: View) : RecyclerView.ViewHolder(convertView)
}