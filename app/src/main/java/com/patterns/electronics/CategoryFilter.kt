package com.patterns.electronics

class CategoryFilter : FilterStrategy {


    override fun filter(search: String, list: ArrayList<Item>): ArrayList<Item> {


        val temp = ArrayList<Item>()


        for(i in list)
        {
            if(i.category.toLowerCase().startsWith(search))
            {
                temp.add(i)
            }

        }

        return temp
    }



}