package me.ranieripieper.android.coffee.core.viewmodel

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

class ResourceManager(private val context: Context) {

    fun getString(@StringRes stringRes: Int): String {
        return context.getString(stringRes)
    }

    fun getString(@StringRes stringRes: Int, vararg formatArgs: Any): String {
        return context.getString(stringRes, *formatArgs)
    }

    fun getQuantityString(@PluralsRes stringRes: Int, count: Int): String {
        return context.resources.getQuantityString(stringRes, count)
    }

    fun getQuantityString(@PluralsRes stringRes: Int, count: Int, vararg formatArgs: Any): String {
        return context.resources.getQuantityString(stringRes, count, *formatArgs)
    }
}
