package com.lust.app.data.utils

import com.google.gson.Gson

class JsonMapper(private val gson: Gson) {

    fun <T> mapJsonToDataClass(jsonString: String, dataClass: Class<T>): T {
        return gson.fromJson(jsonString, dataClass)
    }

    fun <T> mapDataClassToJson(data: T): String {
        return gson.toJson(data)
    }

}