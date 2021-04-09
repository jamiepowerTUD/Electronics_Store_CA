package com.patterns.electronics

class PriceSort : SortStrategy {


    override fun ascending(list: ArrayList<Item>): ArrayList<Item> {
        list.sortBy { it.price }
        return list
    }

    override fun descending(list: ArrayList<Item>): ArrayList<Item> {
        list.sortByDescending { it.price }
        return list
    }
}