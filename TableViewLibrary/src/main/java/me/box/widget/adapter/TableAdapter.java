/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.adapter;

import android.database.DataSetObserver;
import android.support.annotation.IntRange;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by box on 2017/6/29.
 * <p>
 * 表哥布局的适配器
 */

public interface TableAdapter {

    void registerDataSetObserver(@NonNull DataSetObserver observer);

    void unregisterDataSetObserver(@NonNull DataSetObserver observer);

    @MainThread
    void notifyDataSetChanged();

    @MainThread
    void notifyDataSetInvalidated();

    View getColumnHeaderView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int columnIndex);

    View getRowHeaderView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int rowIndex);

    View getValueView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int columnIndex, int rowIndex);

    @IntRange(from = 0)
    int getColumnCount();

    @IntRange(from = 0)
    int getRowCount();

    boolean isEmpty();

    boolean isColumnEmpty();

    boolean isRowEmpty();

    boolean areAllItemsEnabled();

    boolean isValueEnabled(int columnIndex, int rowIndex);

    boolean isColumnEnabled(int columnIndex);

    boolean isRowEnabled(int rowIndex);
}
