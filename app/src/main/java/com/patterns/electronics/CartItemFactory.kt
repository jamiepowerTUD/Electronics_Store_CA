package com.patterns.electronics


/**
 *
 *  The "object" keyword in kotlin enforces a singleton ,
 *  The object gets instantiated when it is used for the first time.
 *
 */
object CartItemFactory {


    var flyweights = HashMap<String, CartItemFlyweight>()


    fun getCartItemFlyweight(name : String,id : String,man : String, uri : String) : CartItemFlyweight
    {

        var item = flyweights[name]

        if(item == null)
        {
            item = CartItemFlyweight(id,name,man,uri)
            flyweights[name] = item
        }

        return item
    }



}