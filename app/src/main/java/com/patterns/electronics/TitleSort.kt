package com.patterns.electronics

class TitleSort : SortStrategy {


    override fun ascending(list: ArrayList<Item>): ArrayList<Item> {
        list.sortBy { it.name }
        return list
    }

    override fun descending(list: ArrayList<Item>): ArrayList<Item> {
        list.sortByDescending { it.name }
        return list
    }
}