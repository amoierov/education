package com.mstagency.domrubusiness.base

interface IBaseRecyclerAdapter<T : Any> {

    fun setItems(newItems: List<T>): Boolean

    fun addItems(newItems: List<T>): Boolean

    fun getItems(): List<T>
}