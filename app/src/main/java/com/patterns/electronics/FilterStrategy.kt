package com.patterns.electronics

interface FilterStrategy
{

    fun filter(search : String, list : ArrayList<Item>) : ArrayList<Item>


}