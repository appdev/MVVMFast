package com.apkdv.mvvmfast.base.integration

import android.content.Context
import com.apkdv.mvvmfast.base.delegate.AppLifecycles

/**
 * [ConfigModule] 可以给框架配置一些参数,需要实现 [ConfigModule] 后,在 AndroidManifest 中声明该实现类
 */
interface ConfigModule {
    /**
     * 全局配置
     *
     * @param context
     */
    fun applyOptions(context: Context?)

    /**
     * 使用[AppLifecycles]在Application的生命周期中注入一些操作
     *
     * @param context
     * @param lifecycles
     */
    fun injectAppLifecycle(context: Context, lifecycles: MutableList<AppLifecycles>)
}