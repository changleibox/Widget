package me.box.widget.ui;

import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

/**
 * Created by box on 2017/6/28.
 * <p>
 * View的兼容类
 */

@SuppressWarnings("deprecation")
class ViewCompat {

    static void addOnceOnGlobalLayoutListener(final View view, final OnGlobalLayoutListener listener) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (listener != null) {
                    listener.onGlobalLayout();
                }
            }
        });
    }
}
