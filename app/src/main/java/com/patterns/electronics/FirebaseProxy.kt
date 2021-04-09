package com.patterns.electronics

interface FirebaseProxy {

    fun add(vararg ts: Any)

    fun update(obj : String, key : String , value : Any)

    fun delete(obj : String)


}