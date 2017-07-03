/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Box on 2017/7/2.
 * <p>
 * baseTableAdapter的升级版本
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class ArrayAdapter<Column, Row, Value> extends BaseAdapter {

    private final List<Column> mColumns = new ArrayList<>();
    private final List<Row> mRows = new ArrayList<>();
    private final SparseArray<SparseArray<Value>> mValues = new SparseArray<>();

    private final Object mLock = new Object();
    private boolean mNotifyOnChange = true;

    @NonNull
    @Override
    public final View getColumnHeaderView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int columnIndex) {
        return getColumnHeaderView(inflater, parent, getColumn(columnIndex), columnIndex);
    }

    @NonNull
    @Override
    public final View getRowHeaderView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int rowIndex) {
        return getRowHeaderView(inflater, parent, getRow(rowIndex), rowIndex);
    }

    @NonNull
    @Override
    public final View getValueView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int columnIndex, int rowIndex) {
        return getValueView(inflater, parent, getValue(columnIndex, rowIndex), columnIndex, rowIndex);
    }

    @Override
    public int getColumnCount() {
        return mColumns.size();
    }

    @Override
    public int getRowCount() {
        return mRows.size();
    }

    public Column getColumn(int columnIndex) {
        return mColumns.get(columnIndex);
    }

    public Row getRow(int rowIndex) {
        return mRows.get(rowIndex);
    }

    @Nullable
    public Value getValue(int columnIndex, int rowIndex) {
        SparseArray<Value> sparseArray = mValues.get(columnIndex);
        return sparseArray == null ? null : sparseArray.get(rowIndex);
    }

    public void addColumn(Column column) {
        synchronized (mLock) {
            mColumns.add(column);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addRow(Row row) {
        synchronized (mLock) {
            mRows.add(row);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void addValue(int columnIndex, int rowIndex, Value value) {
        synchronized (mLock) {
            addValueNotNotify(columnIndex, rowIndex, value);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void setColumns(List<Column> columns) {
        synchronized (mLock) {
            mColumns.clear();
            mColumns.addAll(columns);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void setRows(List<Row> rows) {
        synchronized (mLock) {
            mRows.clear();
            mRows.addAll(rows);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void setValuesToRow(int rowIndex, List<Value> values) {
        synchronized (mLock) {
            for (int columnIndex = 0; columnIndex < values.size(); columnIndex++) {
                addValueNotNotify(columnIndex, rowIndex, values.get(columnIndex));
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void setValuesToColumn(int columnIndex, List<Value> values) {
        synchronized (mLock) {
            for (int rowIndex = 0; rowIndex < values.size(); rowIndex++) {
                addValueNotNotify(columnIndex, rowIndex, values.get(rowIndex));
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void setValues(SparseArray<SparseArray<Value>> values) {
        synchronized (mLock) {
            mValues.clear();
            for (int i = 0; i < values.size(); i++) {
                int columnIndex = values.keyAt(i);
                mValues.put(columnIndex, values.get(columnIndex));
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            mColumns.clear();
            mRows.clear();
            mValues.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    protected abstract View getColumnHeaderView(LayoutInflater inflater, ViewGroup parent, Column column, int columnIndex);

    protected abstract View getRowHeaderView(LayoutInflater inflater, ViewGroup parent, Row row, int rowIndex);

    protected abstract View getValueView(LayoutInflater inflater, ViewGroup parent, @Nullable Value value, int columnIndex, int rowIndex);

    private void addValueNotNotify(int columnIndex, int rowIndex, Value value) {
        SparseArray<Value> sparseArray = mValues.get(columnIndex);
        if (sparseArray == null) {
            sparseArray = new SparseArray<>();
        }
        sparseArray.put(rowIndex, value);
        mValues.put(columnIndex, sparseArray);
    }
}
