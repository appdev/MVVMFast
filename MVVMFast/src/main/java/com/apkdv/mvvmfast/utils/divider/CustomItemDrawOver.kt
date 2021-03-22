package com.apkdv.mvvmfast.utils.divider

import android.graphics.Canvas
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by JinJc on 2017/9/1.
 */
interface CustomItemDrawOver {
    fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State)
}