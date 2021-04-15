package com.patterns.electronics

class TitleFilter : FilterStrategy
{


    override fun filter(search: String, list: ArrayList<Item>) : ArrayList<Item> {

        val temp = ArrayList<Item>()


            for(i in list)
            {
                if(i.name.toLowerCase().startsWith(search))
                {
                    temp.add(i)
                }
            }

        return temp
    }
}