package com.apkdv.mvvmfast.base.delegate

import android.app.Application
import android.content.Context
import com.apkdv.mvvmfast.base.integration.ConfigModule
import com.apkdv.mvvmfast.base.integration.ManifestParser
import com.blankj.utilcode.util.LogUtils

/**
 * desc :AppDelegate 可以代理 Application 的生命周期,在对应的生命周期,执行对应的逻辑
 */
class AppDelegate(context: Context) : AppLifecycles {
    private var mModules: MutableList<ConfigModule>? = null
    private val mAppLifecycles by lazy { arrayListOf<AppLifecycles>() }

    init {
        //用反射, 将 AndroidManifest.xml 中带有 ConfigModule 标签的 class 转成对象集合（List<ConfigModule>）
        mModules = ManifestParser(context).parse()
        //遍历之前获得的集合, 执行每一个 ConfigModule 实现类的某些方法
        mModules?.let { module ->
            //遍历之前获得的集合, 执行每一个 ConfigModule 实现类的某些方法
            //将框架外部, 开发者实现的 Application 的生命周期回调 (AppLifecycles) 存入 mAppLifecycles 集合 (此时还未注册回调)
            module.forEach { it.injectAppLifecycle(context, mAppLifecycles) }
        }
    }

    override fun attachBaseContext(base: Context) {
        //遍历 mAppLifecycles, 执行所有已注册的 AppLifecycles 的 attachBaseContext() 方法 (框架外部, 开发者扩展的逻辑)
        mAppLifecycles.forEach { it.attachBaseContext(base) }
    }

    override fun onCreate(application: Application) {
        mModules?.let {
            it.forEach { module -> module.applyOptions(application) }
        }
        mModules = null

        //执行框架外部, 开发者扩展的 App onCreate 逻辑
        mAppLifecycles.forEach {
            it.onCreate(application)
        }
    }

    override fun onTerminate(application: Application) {
        mAppLifecycles.forEach { lifecycle -> lifecycle.onTerminate(application) }
        mAppLifecycles.clear()
    }

}