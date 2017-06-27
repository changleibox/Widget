package com.box.me.widget;

import android.content.Context;
import android.support.v7.widget.ContentFrameLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

/**
 * Created by Box on 2017/6/27.
 * <p>
 * 表格控件，可上下左右滑动
 */

public class TableView extends ContentFrameLayout {

    public TableView(Context context) {
        super(context);
    }

    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializationLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_table_view, this, true);
    }
}
