package com.apkdv.mvvmfast.base

import com.google.gson.annotations.SerializedName
import com.pandaq.rxpanda.entity.IApiData

data class BaseResult<T>(
    @SerializedName("code", alternate = ["status"]) private val code: Long,
    @SerializedName("message", alternate = ["rinfo","info"]) private val msg: String,
    @SerializedName("data") private val data: T
) : IApiData<T> {
    override fun getCode(): Long {
        return code
    }

    override fun getMsg(): String {
        return msg
    }

    override fun getData(): T {
        return data
    }

    override fun isSuccess(): Boolean {
        return code.toInt() == 200
    }

}