package com.mstagency.domrubusiness.base

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

open class BasePagerAdapter<T : Fragment>(fragment: Fragment, private val items: List<() -> T>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment = items[position].invoke()

}