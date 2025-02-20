package com.mstagency.domrubusiness.base

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mstagency.domrubusiness.R

class BaseRecyclerViewItemDecoratorWithDivider(
    private val context: Context,
    private val firstLastOffset: Int,
    private val elementsOffset: Int,
    private val color: Int,
    private val firstDivider: Boolean,
    private val lastDivider: Boolean
) : RecyclerView.ItemDecoration() {

    private val divider: Drawable? =
        ContextCompat.getDrawable(context, R.drawable.shape_recycler_divider)?.apply {
            setTint(ContextCompat.getColor(context, color))
        }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val mode = (parent.layoutManager as LinearLayoutManager).orientation
        parent.adapter?.let { adapter ->
            when (mode) {
                RecyclerView.HORIZONTAL -> {
                    outRect.right =
                        if (parent.getChildAdapterPosition(view) == adapter.itemCount - 1) firstLastOffset else elementsOffset
                    outRect.left =
                        if (parent.getChildAdapterPosition(view) == 0) firstLastOffset else 0
                }
                RecyclerView.VERTICAL -> {
                    outRect.bottom =
                        if (parent.getChildAdapterPosition(view) == adapter.itemCount - 1) firstLastOffset else elementsOffset
                    outRect.top =
                        if (parent.getChildAdapterPosition(view) == 0) firstLastOffset else 0
                }
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val mode = (parent.layoutManager as LinearLayoutManager).orientation
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            divider?.let {
                when (mode) {
                    RecyclerView.VERTICAL -> {
                        val left = parent.paddingLeft
                        val right = parent.width - parent.paddingRight
                        if (i == 0 && firstDivider) {
                            val firstTop = child.top - firstLastOffset / 2
                            val firstBottom = firstTop + divider.intrinsicHeight
                            divider.setBounds(left, firstTop, right, firstBottom)
                            divider.draw(c)
                        }
                        val bottom = child.bottom + elementsOffset / 2
                        val top = bottom - divider.intrinsicHeight

                        if (i == parent.childCount - 1 && !lastDivider) return

                        divider.setBounds(left, top, right, bottom)
                        divider.draw(c)
                    }
                    RecyclerView.HORIZONTAL -> {
                        val top = parent.paddingTop
                        val bottom = parent.height - parent.paddingBottom
                        if (i == 0 && firstDivider) {
                            val firstLeft = child.left - firstLastOffset / 2
                            val firstRight = firstLeft + divider.intrinsicHeight
                            divider.setBounds(firstLeft, top, firstRight, bottom)
                            divider.draw(c)
                        }
                        val left = child.left + elementsOffset / 2
                        val right = left - divider.intrinsicHeight

                        if (i == parent.childCount - 1 && !lastDivider) return

                        divider.setBounds(left, top, right, bottom)
                        divider.draw(c)
                    }
                }
            }
        }
    }
}