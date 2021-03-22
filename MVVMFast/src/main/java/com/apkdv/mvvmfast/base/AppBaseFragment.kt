package com.apkdv.mvvmfast.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.apkdv.mvvmfast.R
import com.apkdv.mvvmfast.event.Message
import com.apkdv.mvvmfast.utils.StateLayout
import com.blankj.utilcode.util.ToastUtils
import java.lang.reflect.ParameterizedType

/**
 *   @auther : lengyue
 *   time   : 2019/11/01
 */
abstract class AppBaseFragment: Fragment() {


    //是否第一次加载
    private var isFirst: Boolean = true

    private var dialog: MaterialDialog? = null
    protected var mStateLayout: StateLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onVisible()
        beforeViewInit()
        initView(view)
        getData()
    }

    open fun getData() {
    }

    open fun beforeViewInit(){}

    abstract fun initView(view: View)


    override fun onResume() {
        super.onResume()
        onVisible()
    }

    abstract fun layoutId(): Int

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }

    /**
     * 懒加载
     */
    open fun lazyLoadData() {}


    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    protected fun showLoading() {
        if (dialog == null) {
            dialog = context?.let {
                MaterialDialog(it)
                    .cancelable(false)
                    .cornerRadius(8f)
                    .customView(R.layout.custom_progress_dialog_view, noVerticalPadding = true)
                    .lifecycleOwner(this)
                    .maxWidth(R.dimen.dialog_width)
            }
        }
        dialog?.show()
    }
    /***
     * 初始化状态布局layout
     * @param stateLayout： StateLayout
     */
    fun initStateLayout(stateLayout: StateLayout?) {
        if (stateLayout == null) {
            return
        }
        this.mStateLayout = stateLayout
        mStateLayout?.setErrorClick { refreshDataWhenError(AppBaseActivity.STATE_REFRESH_MANUAL) }
        mStateLayout?.setEmptyClick { refreshDataWhenError(AppBaseActivity.STATE_REFRESH_MANUAL) }
    }

    /***
     * 显示无数据状态
     */
    open fun showEmptyState() {
        mStateLayout?.showEmptyView()
    }

    /***
     * 显示错误状态
     */
    open fun showErrorState() {
        mStateLayout?.showErrorView()
    }

    /***
     * 显示错误状态
     */
    open fun showErrorState(errorMsg: String?) {
        mStateLayout?.errorView?.findViewById<TextView>(R.id.vs_lf_loading_fail_txt)?.text =
            if (errorMsg.isNullOrEmpty()) "数据跑哪去了，刷新试试" else errorMsg
        mStateLayout?.showErrorView()
    }


    /***
     * 显示正在加载状态
     */
    open fun showLoadingState() {
        if (mStateLayout != null) {
            mStateLayout?.showLoadingView()
        }
    }

    /***
     * 显示无网络状态
     */
    open fun showNoNetState() {
        if (mStateLayout != null) {
            mStateLayout?.showNoNetView()
        }
    }

    /***
     * 显示内容，即正常页面
     */
    open fun showContentState() {
        mStateLayout?.showContentView()
    }

    /***
     * 当错误页面点击刷新或网络连接后自动刷新时调用
     * @param state
     * [.STATE_REFRESH_MANUAL] 错误页面手动点击刷新
     * [.STATE_REFRESH_NET_CHANGE] 网络连接后自动刷新
     */
    open fun refreshDataWhenError(state: Int) {
        showLoadingState()
    }
    /**
     * 关闭等待框
     */
    protected fun dismissLoading() {
        dialog?.run { if (isShowing) dismiss() }
    }

    /**
     * 注册 UI 事件
     */
    protected fun registerDefUIChange(viewModel: BaseViewModel) {
        viewModel.defUI.showDialog.observe(this, {
            showLoading()
        })
        viewModel.defUI.dismissDialog.observe(this, {
            dismissLoading()
        })
        viewModel.defUI.toastEvent.observe(this, {
            ToastUtils.showShort(it)
        })
        viewModel.defUI.msgEvent.observe(this, {
            handleEvent(it)
        })
    }


    /**
     * 注册 UI 事件
     */
    protected fun registerLayoutChange(viewModel: BaseViewModel) {
        viewModel.defLayout.showContent.observe(this, {
            showContentState()
        })
        viewModel.defLayout.showEmpty.observe(this, {
            showEmptyState()
        })
        viewModel.defLayout.showError.observe(this, {
            showErrorState()
        })
        viewModel.defLayout.showLoading.observe(this, {
            showLoadingState()
        })
        viewModel.defLayout.showNoNet.observe(this, {
            showNoNetState()
        })
        viewModel.defLayout.showErrorMsg.observe(this, {
            showErrorState(it)
        })
    }


}