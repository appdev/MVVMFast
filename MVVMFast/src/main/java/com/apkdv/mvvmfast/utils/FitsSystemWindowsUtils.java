package com.apkdv.mvvmfast.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

/**
 * Created by ZhaoShulin on 2019-11-25 15:20.
 * <br>
 * Desc: FitsSystemWindows
 * <br>
 */
public class FitsSystemWindowsUtils {


    /**
     * 调用系统view的setFitsSystemWindows方法
     * Sets fits system windows.
     *
     * @param activity        the activity
     * @param applySystemFits the apply system fits
     */
    private static void setFitsSystemWindows(Activity activity, boolean applySystemFits) {
        if (activity == null) {
            return;
        }
        setFitsSystemWindows(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0), applySystemFits);
    }

    public static void setFitsSystemWindows(Activity activity) {
        setFitsSystemWindows(activity, true);
    }

    public static void setFitsSystemWindows(Fragment fragment, boolean applySystemFits) {
        if (fragment == null) {
            return;
        }
        setFitsSystemWindows(fragment.getActivity(), applySystemFits);
    }

    public static void setFitsSystemWindows(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        setFitsSystemWindows(fragment.getActivity());
    }

    private static void setFitsSystemWindows(View view, boolean applySystemFits) {
        if (view == null) {
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (viewGroup instanceof DrawerLayout) {
                setFitsSystemWindows(viewGroup.getChildAt(0), applySystemFits);
            } else {
                viewGroup.setFitsSystemWindows(applySystemFits);
                viewGroup.setClipToPadding(true);
            }
        } else {
            view.setFitsSystemWindows(applySystemFits);
        }
    }

    /**
     * 检查布局根节点是否使用了android:fitsSystemWindows="true"属性
     * Check fits system windows boolean.
     *
     * @param view the view
     * @return the boolean
     */
    public static boolean checkFitsSystemWindows(View view) {
        if (view == null) {
            return false;
        }
        if (view.getFitsSystemWindows()) {
            return true;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0, count = viewGroup.getChildCount(); i < count; i++) {
                View childView = viewGroup.getChildAt(i);
                if (childView instanceof DrawerLayout) {
                    if (checkFitsSystemWindows(childView)) {
                        return true;
                    }
                }
                if (childView.getFitsSystemWindows()) {
                    return true;
                }
            }
        }
        return false;
    }
}
