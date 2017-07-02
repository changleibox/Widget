/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.adapter;

import android.support.annotation.NonNull;
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

public abstract class ArrayAdapter<Column, Row, Value> extends BaseAdapter {

    private final List<Column> mColumns = new ArrayList<>();
    private final List<Row> mRows = new ArrayList<>();
    private final SparseArray<SparseArray<Value>> mValues = new SparseArray<>();

    @NonNull
    @Override
    public View getColumnHeaderView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int columnIndex) {
        return getColumnHeaderView(inflater, parent, mColumns.get(columnIndex), columnIndex);
    }

    @NonNull
    @Override
    public View getRowHeaderView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int rowIndex) {
        return getRowHeaderView(inflater, parent, mRows.get(rowIndex), rowIndex);
    }

    @NonNull
    @Override
    public View getValueView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int columnIndex, int rowIndex) {
        return getValueView(inflater, parent, mValues.get(columnIndex).get(rowIndex), columnIndex, rowIndex);
    }

    @Override
    public int getColumnCount() {
        return mColumns.size();
    }

    @Override
    public int getRowCount() {
        return mRows.size();
    }



    protected abstract View getColumnHeaderView(LayoutInflater inflater, ViewGroup parent, Column column, int columnIndex);

    protected abstract View getRowHeaderView(LayoutInflater inflater, ViewGroup parent, Row row, int rowIndex);

    protected abstract View getValueView(LayoutInflater inflater, ViewGroup parent, Value value, int columnIndex, int rowIndex);
}
