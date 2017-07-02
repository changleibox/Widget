/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import java.util.List;

import me.box.widget.R;
import me.box.widget.adapter.TableAdapter;

/**
 * Created by box on 2017/6/26.
 * <p>
 * 测试
 */

@SuppressWarnings({"deprecation", "WeakerAccess"})
public final class ValueAdapter extends RecyclerView.Adapter<TableViewHolder> {

    private TableAdapter mAdapter;
    private LayoutInflater mInflater;

    private Table mTable;

    @Nullable
    private OnValueClickListener mValueClickListener;

    public ValueAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setOnValueClickListener(OnValueClickListener listener) {
        this.mValueClickListener = listener;
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TableViewHolder(mInflater.inflate(R.layout.widget_item_table, parent, false));
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        Table.Row row = mTable.getRows().get(position);
        List<Table.Value> values = row.getValues();

        ViewGroup parent = (ViewGroup) holder.itemView;

        parent.removeAllViews();
        for (int columnIndex = 0; columnIndex < mAdapter.getColumnCount(); columnIndex++) {
            View valueView = mAdapter.getValueView(mInflater, parent, columnIndex, position);
            LayoutParams params;
            if (valueView == null) {
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                valueView = new View(mInflater.getContext());
                valueView.setEnabled(false);
            } else {
                params = valueView.getLayoutParams();
            }
            params.width = mTable == null ? 0 : mTable.getColumnWidth(columnIndex);
            params.height = row.getHeight();
            parent.addView(valueView, params);

            valueView.setEnabled(valueView.isEnabled() && mAdapter.areAllItemsEnabled()
                    && mAdapter.isValueEnabled(columnIndex, position));

            final Table.Value value = columnIndex >= values.size() ? new Table.Value() : values.get(columnIndex);

            valueView.setOnClickListener(view -> {
                if (mValueClickListener != null) {
                    mValueClickListener.onValueClick(value);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mTable == null
                || mAdapter == null
                || mAdapter.isEmpty()
                || mAdapter.isRowEmpty()
                || mAdapter.isColumnEmpty() ? 0 : mTable.getRows().size();
    }

    public void setTableAdapter(TableAdapter tableAdapter) {
        this.mAdapter = tableAdapter;
        notifyDataSetChanged();
    }

    public void setTable(Table table) {
        mTable = table;
        notifyDataSetChanged();
    }

    public interface OnValueClickListener {
        void onValueClick(Table.Value value);
    }

}
