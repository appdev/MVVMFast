package com.apkdv.mvvmfast.base

import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 *   time   : 2019/11/01
 */
abstract class ViewModelFragment<VM : BaseViewModel> : AppBaseFragment() {
    protected lateinit var viewModel: VM

    override fun beforeViewInit() {
        createViewModel()
        lifecycle.addObserver(viewModel)
        //注册 UI事件
        registerDefUIChange(viewModel)
        registerLayoutChange(viewModel)
        super.beforeViewInit()
    }

    /**
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this, ViewModelFactory()).get(tClass) as VM
        }
    }

}