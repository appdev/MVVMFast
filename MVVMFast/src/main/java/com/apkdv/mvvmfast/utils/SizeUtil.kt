package com.apkdv.mvvmfast.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.blankj.utilcode.util.Utils

object SizeUtil {

    fun hideSoftInputKeyBoard(
        context: Context,
        focusView: View?
    ) {
        if (focusView != null) {
            val binder = focusView.windowToken
            if (binder != null) {
                val imd =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imd.hideSoftInputFromWindow(
                    binder,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
            }
        }
    }

    fun showSoftInputKeyBoard(
        context: Context,
        focusView: View?
    ) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(focusView, InputMethodManager.SHOW_FORCED)
    }

    fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    @SuppressLint("PrivateApi")
    fun getStatusBarHeight(): Int {
        var statusBarHeight = 0
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val obj = c.newInstance()
            val field = c.getField("status_bar_height")
            val x = field[obj].toString().toInt()
            statusBarHeight = Resources.getSystem().getDimensionPixelSize(x)
        } catch (e1: Exception) {
            e1.printStackTrace()
        }
        return statusBarHeight
    }

    fun getAppInScreenheight(): Int {
        return getScreenHeight() - getStatusBarHeight();
    }

    fun getWidth(): Int {
       return  Utils.getApp().resources.displayMetrics.widthPixels
    }
    fun getHeight(): Int {
        return  Utils.getApp().resources.displayMetrics.heightPixels
    }
}