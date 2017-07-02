/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;

/**
 * Created by box on 2017/6/29.
 * <p>
 * tableView的适配器
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseAdapter implements TableAdapter {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public BaseAdapter() {
    }

    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    @Override
    public void registerDataSetObserver(@NonNull DataSetObserver var1) {
        mDataSetObservable.registerObserver(var1);
    }

    @Override
    public void unregisterDataSetObserver(@NonNull DataSetObserver var1) {
        mDataSetObservable.unregisterObserver(var1);
    }

    @Override
    public boolean isEmpty() {
        return isColumnEmpty() && isRowEmpty();
    }

    @Override
    public boolean isColumnEmpty() {
        return getColumnCount() == 0;
    }

    @Override
    public boolean isRowEmpty() {
        return getRowCount() == 0;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isValueEnabled(int columnIndex, int rowIndex) {
        return true;
    }

    @Override
    public boolean isColumnEnabled(int columnIndex) {
        return true;
    }

    @Override
    public boolean isRowEnabled(int rowIndex) {
        return true;
    }
}
