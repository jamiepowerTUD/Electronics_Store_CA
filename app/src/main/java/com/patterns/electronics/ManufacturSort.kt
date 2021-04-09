package com.patterns.electronics

class ManufacturSort : SortStrategy {

    override fun ascending(list: ArrayList<Item>): ArrayList<Item> {
        list.sortBy { it.manufacturer }
        return list
    }

    override fun descending(list: ArrayList<Item>): ArrayList<Item> {
        list.sortByDescending { it.manufacturer }
        return list
    }
}