package com.apkdv.mvvmfast.base

import android.app.Application
import android.content.Context
import com.apkdv.mvvmfast.base.delegate.AppDelegate
import com.apkdv.mvvmfast.base.delegate.AppLifecycles
import com.blankj.utilcode.util.Utils

/**
 * desc :BaseApplication
 */
class BaseApplication : Application() {
    private var mAppDelegate: AppLifecycles? = null
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        if (mAppDelegate == null) mAppDelegate = AppDelegate(base)
        mAppDelegate?.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        mAppDelegate?.onCreate(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        mAppDelegate?.onTerminate(this)
    }

}