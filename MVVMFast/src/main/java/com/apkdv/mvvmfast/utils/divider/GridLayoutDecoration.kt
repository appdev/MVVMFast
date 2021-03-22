package com.apkdv.mvvmfast.utils.divider

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridLayoutDecoration(
    private val space: Int,
    private val column: Int,
    val showEnd: Boolean = true
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        // 每列分配的间隙大小，包括左间隙和右间隙
        val colPadding = space * (column + 1) / column
        // 列索引
        val colIndex = position % column
        // 列左、右空隙。右间隙=space-左间隙
        if (colIndex == 0 && showEnd) {
            outRect.left = space
        } else {
            outRect.left = space / 2
        }
        if (colIndex != column && showEnd) {
            outRect.right = space
        } else {
            outRect.right = space / 2
        }
        // 行间距
        if (position >= column) {
            outRect.top = space
        }
    }
}