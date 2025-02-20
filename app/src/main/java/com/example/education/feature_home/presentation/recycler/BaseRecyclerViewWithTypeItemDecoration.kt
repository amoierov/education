package com.mstagency.domrubusiness.base

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BaseRecyclerViewWithTypeItemDecoration(private val viewTypesList: List<ViewTypeOffset>) :
    RecyclerView.ItemDecoration() {

    class ViewTypeOffset(val viewType: Int, var firstLastOffset: Int, var elementsOffset: Int)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)

        val viewType =
            if (position >= 0 && position < (parent.adapter?.itemCount ?: 0))
                parent.adapter?.getItemViewType(position)
            else
                null
        val mode = (parent.layoutManager as LinearLayoutManager).orientation
        parent.adapter?.let { adapter ->
            when (mode) {
                RecyclerView.HORIZONTAL -> {
                    viewTypesList.find { it.viewType == viewType }?.apply {
                        outRect.right =
                            if (position == adapter.itemCount - 1) firstLastOffset else elementsOffset
                        outRect.left = if (position == 0) firstLastOffset else 0
                    }
                }

                RecyclerView.VERTICAL -> {
                    viewTypesList.find { it.viewType == viewType }?.apply {
                        outRect.bottom =
                            if (position == adapter.itemCount - 1) firstLastOffset else elementsOffset
                        outRect.top =
                            if (position == 0) firstLastOffset else 0
                    }
                }

                else -> Unit
            }
        }
    }

}