package com.apkdv.mvvmfast.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkdv.mvvmfast.R;


/**
 * 显示不同状态的布局
 */

public class StateLayout extends FrameLayout {
    private final int StateContent = -1, StateLoading = 0, StateError = 1, StateEmpty = 2, StateNoNet = 3;
    /**
     * 四种状态布局
     **/
    private View noNetworkView, loadingView, errorView, noDataView;
    private TextView emptyHintView;
    private ImageView emptyImageView;
    /***
     * 存放所有的布局文件
     **/
    private View mContentView;

    public int getShowState() {
        return mShowState;
    }

    /***
     * 当前显示的布局
     */
    private int mShowState = StateLoading;
    private boolean isSubLayer = false;
    private ImageView mLoadingImg;

    public StateLayout(Context context) {
        this(context, null);
    }

    public StateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.StateLayout, defStyleAttr, 0);
        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (array.hasValue(R.styleable.StateLayout_noNetLayout)) {
                int noNetId = array.getResourceId(R.styleable.StateLayout_noNetLayout, R.layout.default_viewstatus_no_netwrok);
                noNetworkView = inflater.inflate(noNetId, null);
                setViewVisibility(noNetworkView, false);
            } else {
                checkNullAndInflate(StateNoNet);
            }

            if (array.hasValue(R.styleable.StateLayout_emptyLayout)) {
                int emptyId = array.getResourceId(R.styleable.StateLayout_emptyLayout, R.layout.default_viewstatus_no_data);
                noDataView = inflater.inflate(emptyId, null);
                setViewVisibility(noDataView, false);
            } else {
                checkNullAndInflate(StateEmpty);
            }

            if (array.hasValue(R.styleable.StateLayout_errorLayout)) {
                int errorId = array.getResourceId(R.styleable.StateLayout_errorLayout, R.layout.default_viewstatus_loading_fail);
                errorView = inflater.inflate(errorId, null);
                setViewVisibility(errorView, false);
            } else {
                checkNullAndInflate(StateError);
            }

            if (array.hasValue(R.styleable.StateLayout_loadingLayout)) {
                int loadingId = array.getResourceId(R.styleable.StateLayout_loadingLayout, R.layout.default_viewstatus_loading);
                loadingView = inflater.inflate(loadingId, null);

                setViewVisibility(loadingView, false);
            } else {
                checkNullAndInflate(StateLoading);
            }

            if (array.hasValue(R.styleable.StateLayout_isSubLayer)) {
                isSubLayer = array.getBoolean(R.styleable.StateLayout_isSubLayer, false);
            }
        } finally {
            array.recycle();
        }
        // 默认显示正在加载
        showLoadingView();
    }

    /**
     * 检查某个控件是否为空，并初始化
     *
     * @param state
     */
    private void checkNullAndInflate(int state) {
        View pView = null;
        LayoutInflater inflater = LayoutInflater.from(getContext());
//        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        switch (state) {
            case StateEmpty:
                if (noDataView == null) {
                    noDataView = inflater.inflate(R.layout.default_viewstatus_no_data, null);
                    emptyHintView = noDataView.findViewById(R.id.state_layout_empty_hint);
                    emptyImageView = noDataView.findViewById(R.id.state_layout_empty_img);
                    pView = noDataView;
                }
                break;
            case StateError:
                if (errorView == null) {
                    errorView = inflater.inflate(R.layout.default_viewstatus_loading_fail, null);
                    TextView failTxtTV = errorView.findViewById(R.id.vs_lf_loading_fail_txt);
                    SpannableString styledText = new SpannableString("数据跑哪去了，刷新试试");
                    styledText.setSpan(new TextAppearanceSpan(getContext(), R.style.State_fail_txt), 7, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    failTxtTV.setText(styledText);
                    pView = errorView;
                }
                break;
            case StateLoading:
                if (loadingView == null) {
                    loadingView = inflater.inflate(R.layout.default_viewstatus_loading, null);
                    mLoadingImg = loadingView.findViewById(R.id.loading_img);
                    pView = loadingView;
                }
                break;
            case StateNoNet:
                if (noNetworkView == null) {
                    noNetworkView = inflater.inflate(R.layout.default_viewstatus_no_netwrok, null);
                    setNoNetClick(null);
                    pView = noNetworkView;
                }
                break;
        }

        if (pView != null) {
//            addView(pView, params);
            setViewVisibility(pView, false);
        }
    }


    /**
     * 显示空视图
     */
    public void showEmptyView() {
        //checkNullAndInflate(StateEmpty);
        selectView(nowShowView(mShowState), noDataView);
        mShowState = StateEmpty;
    }

    /**
     * 显示没有网络视图
     */
    public void showNoNetView() {
        //checkNullAndInflate(StateNoNet);
        selectView(nowShowView(mShowState), noNetworkView);
        mShowState = StateNoNet;
    }

    /**
     * 显示加载中布局
     */
    public void showLoadingView() {
        //checkNullAndInflate(StateLoading);
        selectView(nowShowView(mShowState), loadingView);
        if (mLoadingImg != null) {
            GlideUtil.getInstance().loadGifUrl(mLoadingImg, R.drawable.mach);
        }
        mShowState = StateLoading;
    }

    /**
     * 显示加载失败视图
     */
    public void showErrorView() {
        //checkNullAndInflate(StateError);
        selectView(nowShowView(mShowState), errorView);
        mShowState = StateError;
    }

    /**
     * 显示内容视图
     */
    public void showContentView() {
        selectView(nowShowView(mShowState), mContentView);
        mShowState = StateContent;
    }

    public void setEmptyHintText(String mEmptyHintText) {
        if (emptyHintView != null) {
            emptyHintView.setText(mEmptyHintText);
        }
    }

    public void setEmptyImageRes(int mEmptyImageRes) {
        if (emptyImageView != null) {
            emptyImageView.setImageResource(mEmptyImageRes);
        }
    }

    /**
     * 设置没有网络时点击事件
     * <p>
     * 有默认点击事件
     *
     * @param pClick 点击回调
     */
    public void setNoNetClick(OnClickListener pClick) {
        if (noNetworkView == null) {
            throw new NullPointerException("view not inflate");
        }

        if (pClick != null) {
            noNetworkView.findViewById(R.id.vs_nn_root).setOnClickListener(pClick);
        } else {
            noNetworkView.findViewById(R.id.vs_nn_root).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    openWifiSetting(getContext());
                }
            });
        }
    }

    /**
     * 设置加载中的点击事件
     *
     * @param pClick 回调
     */
    public void setLoadingClick(OnClickListener pClick) {
        if (pClick != null) {
            if (loadingView == null) {
                throw new NullPointerException("view not inflate");
            }
            loadingView.findViewById(R.id.vs_le_root).setOnClickListener(pClick);
        }
    }

    /**
     * 设置加载失败点击事件
     *
     * @param pClick 点击回调
     */
    public void setErrorClick(OnClickListener pClick) {
        if (pClick != null) {
            if (errorView == null) {
                throw new NullPointerException("view not inflate");
            }
            errorView.findViewById(R.id.vs_lf_root).setOnClickListener(pClick);
        }
    }

    /**
     * 设置没有数据时，点击事件
     *
     * @param pClick 点击回调
     */
    public void setEmptyClick(OnClickListener pClick) {
        if (pClick != null) {
            if (noDataView == null) {
                throw new NullPointerException("view not inflate");
            }
            noDataView.findViewById(R.id.vs_nd_root).setOnClickListener(pClick);
        }
    }

    /**
     * 打开网络设置界面
     *
     * @param pContext 上下文
     */
    private void openWifiSetting(Context pContext) {
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (pContext != null) {
            pContext.startActivity(intent);
        }
    }

    /**
     * 根据状态返回布局
     *
     * @param state
     * @return
     */
    private View nowShowView(int state) {
        View retuView = null;
        switch (state) {
            case StateContent:
                retuView = mContentView;
                break;
            case StateEmpty:
                retuView = noDataView;
                break;
            case StateError:
                retuView = errorView;
                break;
            case StateLoading:
                retuView = loadingView;
                break;
            case StateNoNet:
                retuView = noNetworkView;
                break;
        }
        return retuView;
    }

    /**
     * 从旧布局选择到新布局，可以考虑做动画
     */
    private void selectView(final View pOldView, final View pNewView) {
        setViewVisibility(pOldView, false);
        setViewVisibility(pNewView, true);

        /*setViewVisibility(pOldView, true);
        setViewVisibility(pNewView, true);
        AlphaAnimation oldAlpha = new AlphaAnimation(1, 0);
        oldAlpha.setDuration(500);
        final AlphaAnimation newAlpha = new AlphaAnimation(0, 1);
        newAlpha.setDuration(500);
        pOldView.setAnimation(oldAlpha);
        pNewView.setAnimation(newAlpha);
        oldAlpha.startNow();
        newAlpha.startNow();
        oldAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        newAlpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
    }

    /***
     * 设置一个控件的状态
     *
     * @param pView 控件
     * @param vis   true，显示，false不显示
     */
    private void setViewVisibility(View pView, boolean vis) {
        if (pView != null) {
            pView.setVisibility(vis ? VISIBLE : GONE);
        }
    }

    /***
     * 如果没有设置内容布局ID,除了四个状态布局，其它都算是内容布局
     *
     * @param view
     */
    private void checkIsContentView(View view) {
        if (view != null && view != noNetworkView && view != loadingView && view != errorView && view != noDataView) {
            mContentView = view;
            setViewVisibility(mContentView, false);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView(loadingView, params);
            addView(noDataView, params);
            addView(noNetworkView, params);
            addView(errorView, params);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isSubLayer) {
            checkIsContentView(child);
            super.addView(child, index, params);
        } else {
            super.addView(child, index, params);
            checkIsContentView(child);
        }
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        boolean result;
        if (isSubLayer) {
            checkIsContentView(child);
            result = super.addViewInLayout(child, index, params, preventRequestLayout);
        } else {
            result = super.addViewInLayout(child, index, params, preventRequestLayout);
            checkIsContentView(child);
        }

        return result;
    }

    public View getNoNetworkView() {
        return noNetworkView;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public View getErrorView() {
        return errorView;
    }

    public View getNoDataView() {
        return noDataView;
    }
}
