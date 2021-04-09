package com.patterns.electronics

interface SortStrategy
{

    fun ascending(list : ArrayList<Item>) : ArrayList<Item>

    fun descending(list : ArrayList<Item>) : ArrayList<Item>


}