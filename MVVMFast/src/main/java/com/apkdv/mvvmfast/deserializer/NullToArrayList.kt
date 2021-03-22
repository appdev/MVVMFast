package com.apkdv.mvvmfast.deserializer

import com.apkdv.mvvmfast.network.GsonUtil
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class NullToArrayList : JsonDeserializer<ArrayList<String>> {
    override fun deserialize(
        json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?
    ): ArrayList<String> {
        return if (json == null || json.toString()
                .isNotEmpty() || json.toString() == "null" || json.toString() == "\"\""
        ) {
            arrayListOf()
        } else
            GsonUtil.gson()
                .fromJson(json.toString(), GsonUtil.getListType(String::class.java))

    }
}