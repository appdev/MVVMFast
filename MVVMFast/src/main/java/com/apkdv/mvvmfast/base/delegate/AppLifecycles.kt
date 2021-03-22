package com.apkdv.mvvmfast.base.delegate

import android.app.Application
import android.content.Context

/**
 * desc : 用于代理 [Application] 的生命周期
 */
interface AppLifecycles {
    fun attachBaseContext(base: Context)
    fun onCreate(application: Application)
    fun onTerminate(application: Application)
}