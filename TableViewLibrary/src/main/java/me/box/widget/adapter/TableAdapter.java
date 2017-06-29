package me.box.widget.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by box on 2017/6/29.
 * <p>
 * 表哥布局的适配器
 */

public interface TableAdapter {

    View getColumnHeaderView(LayoutInflater inflater, ViewGroup parent, int columnIndex);

    View getRowHeaderView(LayoutInflater inflater, ViewGroup parent, int rowIndex);

    View getValueView(LayoutInflater inflater, ViewGroup parent, int columnIndex, int rowIndex);

    int getColumnCount();

    int getRowCount();
}
