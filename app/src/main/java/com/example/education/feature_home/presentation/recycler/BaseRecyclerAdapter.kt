package com.mstagency.domrubusiness.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

open class BaseRecyclerAdapter<T : BaseModel, VH : RecyclerView.ViewHolder>(
    private var items: List<T> = listOf(),
    private val viewHolderCreator: (ViewGroup, Int) -> VH,
    private val viewHolderBinder: (holder: VH, item: T, position: Int) -> Unit,
    private val getItemType: (item: T) -> Int = { 0 },
) : RecyclerView.Adapter<VH>(), IBaseRecyclerAdapter<T> {

    private val diffUtilCallback = DiffUtilCallback<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        viewHolderCreator(parent, viewType)

    override fun onBindViewHolder(holder: VH, position: Int) =
        viewHolderBinder(holder, items[position], position)

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = items[position].id

    override fun setItems(newItems: List<T>) = with(ArrayList<T>().apply { addAll(newItems) }) {
        synchronized(items) { items = this }
        dispatchUpdates(items)
        notifyDataSetChanged()
        true
    }

    fun resetItems() {
        val previousContentSize = items.size
        synchronized(items) { items = listOf() }
        if (previousContentSize > 0) {
            notifyItemRangeRemoved(0, previousContentSize)
        }
    }

    override fun addItems(newItems: List<T>) =
        with(items.toMutableList().apply { addAll(newItems) }) {
            synchronized(items) { items = this }
            dispatchUpdates(newItems)
            true
        }

    override fun getItemViewType(position: Int): Int = getItemType(items[position])

    override fun getItems(): List<T> = items

    fun getItem(position: Int) = items[position]

    private fun dispatchUpdates(newItems: List<T>) =
        DiffUtil.calculateDiff(diffUtilCallback.update(newItems)).dispatchUpdatesTo(this)

    class ViewHolder<V : ViewBinding>(val binding: V) : RecyclerView.ViewHolder(binding.root) {
        constructor(
            parent: ViewGroup,
            creator: (inflater: LayoutInflater, root: ViewGroup, attachToRoot: Boolean) -> V,
        ) : this(
            creator(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    private class DiffUtilCallback<T : BaseModel> : DiffUtil.Callback() {

        private val oldItems: MutableList<T> = ArrayList()
        private val newItems: MutableList<T> = ArrayList()

        fun update(newList: List<T>) = apply {
            oldItems.clear()
            oldItems.addAll(newItems)
            newItems.clear()
            newItems.addAll(newList)
        }

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }
}

fun <K : ViewBinding> ViewGroup.viewHolderFrom(
    creator: (inflater: LayoutInflater, root: ViewGroup, attachToRoot: Boolean) -> K,
): BaseRecyclerAdapter.ViewHolder<K> = BaseRecyclerAdapter.ViewHolder(this, creator)

inline fun <reified T : ViewBinding> BaseRecyclerAdapter.ViewHolder<*>.binding() = (binding as T)

fun <T : BaseModel> diffItemCallback(): DiffUtil.ItemCallback<T> {
    return object : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem.id == newItem.id
        }
    }
}

fun <T : BaseModel, VH : RecyclerView.ViewHolder> createAdapter(
    items: List<T>,
    viewHolderCreator: (ViewGroup, Int) -> VH,
    viewHolderBinder: (holder: VH, item: T, position: Int) -> Unit,
): RecyclerView.Adapter<VH> = BaseRecyclerAdapter(items, viewHolderCreator, viewHolderBinder)

fun <T : BaseModel, VH : RecyclerView.ViewHolder> createAdapter(
    items: List<T>,
    viewHolderCreator: (ViewGroup, Int) -> VH,
    viewHolderBinder: (holder: VH, item: T, position: Int) -> Unit,
    getItemType: (item: T) -> Int,
): RecyclerView.Adapter<VH> = BaseRecyclerAdapter(items, viewHolderCreator, viewHolderBinder, getItemType)