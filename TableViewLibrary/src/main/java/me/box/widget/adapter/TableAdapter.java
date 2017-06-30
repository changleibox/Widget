/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.adapter;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by box on 2017/6/29.
 * <p>
 * 表哥布局的适配器
 */

public interface TableAdapter {

    void registerDataSetObserver(DataSetObserver observer);

    void unregisterDataSetObserver(DataSetObserver observer);

    View getColumnHeaderView(LayoutInflater inflater, ViewGroup parent, int columnIndex);

    View getRowHeaderView(LayoutInflater inflater, ViewGroup parent, int rowIndex);

    View getValueView(LayoutInflater inflater, ViewGroup parent, int columnIndex, int rowIndex);

    int getColumnCount();

    int getRowCount();

    boolean isEmpty();

    boolean areAllItemsEnabled();

    boolean isEnabled(int columnIndex, int rowIndex);
}
