package com.wahid.toko.utills;
import android.support.v4.widget.NestedScrollView;

public abstract class NestedScroll implements NestedScrollView.OnScrollChangeListener {

    public void onScrollChange(NestedScrollView view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

        if (view.getChildAt(view.getChildCount() - 1) != null) {
            if ((scrollY >= (view.getChildAt(view.getChildCount() - 1).getMeasuredHeight() - view.getMeasuredHeight())) &&
                    scrollY > oldScrollY) {
                onScroll();
            }

        }
    }

    public abstract void onScroll();
}