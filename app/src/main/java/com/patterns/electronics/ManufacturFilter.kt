package com.patterns.electronics

class ManufacturFilter : FilterStrategy {


    override fun filter(search: String, list: ArrayList<Item>): ArrayList<Item> {

        val temp = ArrayList<Item>()

        for(i in list)
        {
            if(i.manufacturer.equals(search,ignoreCase = true))
            {
                temp.add(i)
            }
        }


        return temp
    }



}