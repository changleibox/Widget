/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.app.testtableview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;

import me.box.app.testtableview.entity.TableData;

public class TableValueView extends AppCompatTextView {

    public TableValueView(Context context) {
        this(context, (TableData.Value) null);
    }

    public TableValueView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableValueView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTableValueView(context, null);
    }

    public TableValueView(Context context, TableData.Value value) {
        super(context);
        initTableValueView(context, value);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        setPadding(padding, 0, padding, 0);
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    private void initTableValueView(Context context, TableData.Value value) {
        setValue(value);
        setGravity(Gravity.CENTER);
        setSingleLine(true);
        setTextAppearance(context, R.style.TextAppearance_AppCompat);
    }

    public void setValue(TableData.Value value) {
        if (value != null) {
            setText(value.getLabel());
        }
    }
}