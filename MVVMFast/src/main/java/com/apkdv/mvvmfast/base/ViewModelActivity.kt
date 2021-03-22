package com.apkdv.mvvmfast.base

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 *   @auther : lengyue
 *   time   : 2019/11/01
 */
abstract class ViewModelActivity<VM : BaseViewModel,T:ViewBinding> : AppBaseActivity<T>() {

    override fun beforeViewInit() {
        createViewModel()
        lifecycle.addObserver(viewModel)
        registerDefUIChange(viewModel)
        registerLayoutChange(viewModel)
        super.beforeViewInit()
    }

    protected lateinit var viewModel: VM

    /**
     * 通过反射的方式自动创建 ViewModel
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    protected fun createViewModel() {
        //返回表示此 Class所表示的实体（类、接口、基本类型或 void）的直接超类的 Type
        // 使用反射技术得到T的真实类型
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            //返回表示此类型实际类型参数的 Type对象的数组。
            // 获取第一个类型参数的真实类型
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this, ViewModelFactory()).get(tClass) as VM
        }
    }

}