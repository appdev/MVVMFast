package com.apkdv.mvvmfast.base

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.apkdv.mvvmfast.R
import com.apkdv.mvvmfast.event.Message
import com.apkdv.mvvmfast.utils.StateLayout
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.apkdv.mvvmfast.view.titlebar.widget.CommonTitleBar
import com.blankj.utilcode.util.ToastUtils


abstract class AppBaseActivity<T : ViewBinding> : AppCompatActivity() {

    private var dialog: MaterialDialog? = null
    protected lateinit var binding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getBindView()
        setContentView(binding.root)
        //注册 UI事件
        beforeViewInit()
        initView(savedInstanceState)
        initData()
        getData()
    }

    abstract fun getBindView(): T

    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData()
    abstract fun getData()

    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    private fun showLoading() {
        if (dialog == null) {
            dialog = MaterialDialog(this)
                .cancelable(false)
                .cornerRadius(8f)
                .customView(R.layout.custom_progress_dialog_view, noVerticalPadding = true)
                .lifecycleOwner(this)
                .maxWidth(R.dimen.dialog_width)
        }
        dialog?.show()

    }

    /**
     * 关闭等待框
     */
    private fun dismissLoading() {
        dialog?.run { if (isShowing) dismiss() }
    }


    companion object {
        const val STATE_REFRESH_MANUAL = 1
        const val STATE_REFRESH_NET_CHANGE = 2
    }


    protected var mStateLayout: StateLayout? = null


    open fun beforeViewInit() {
        if (Build.VERSION.SDK_INT >= 21) {
            val ab = supportActionBar
            if (ab != null) {
                ab.elevation = 0f
            }
        }
        if (supportActionBar != null) {
            StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.colorAccent))
            StatusBarCompat.setDarkStatus(this.window)
            initToolbar()
        }
    }


    val titleBarView: CommonTitleBar by lazy {
        View.inflate(
            this,
            R.layout.toolbar,
            null
        ) as CommonTitleBar
    }

    private fun initToolbar() {
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.setDisplayShowHomeEnabled(false)
            actionBar.setDisplayShowCustomEnabled(true)
            val alp = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT
            )
            actionBar.setCustomView(titleBarView, alp)
            if (titleBarView.parent is Toolbar) {
                val parent = titleBarView.parent as Toolbar
                parent.setContentInsetsAbsolute(0, 0)
            }
            actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            if (enableBack()) {
                val imageTintList = ColorStateList.valueOf(Color.WHITE)
                titleBarView.leftImageButton.imageTintList = imageTintList
                titleBarView.leftImageButton.setOnClickListener {
                    onBackPressed()
                }
            } else titleBarView.leftImageButton.visibility = View.GONE
        }
    }

    open fun setTitleBarColor(@ColorInt titleBarColor: Int) {
        titleBarView.setTitleBarColor(titleBarColor)
    }


    open fun enableBack(): Boolean {
        return true
    }

    fun setTitleName(titleName: String) {
        if (supportActionBar != null) {
            titleBarView.setCenterText(titleName)
        }
    }


    fun setLineVisible(b: Boolean) {
        titleBarView.setShowBottomLine(b)
    }

    fun setToolbarBackground(@ColorInt color: Int) {
        titleBarView.setBackgroundColor(color)
    }


    /***
     * 初始化状态布局layout
     * @param stateLayoutId： StateLayout的id
     */
    fun initStateLayout(stateLayoutId: Int) {
        initStateLayout(findViewById<View>(stateLayoutId) as StateLayout)
    }

    /***
     * 初始化状态布局layout
     * @param stateLayout： StateLayout
     */
    protected fun initStateLayout(stateLayout: StateLayout?) {
        if (stateLayout == null) {
            return
        }
        this.mStateLayout = stateLayout
        mStateLayout?.setErrorClick { refreshDataWhenError(STATE_REFRESH_MANUAL) }
        mStateLayout?.setEmptyClick { refreshDataWhenError(STATE_REFRESH_MANUAL) }
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