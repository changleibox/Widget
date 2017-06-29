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

    private final Context mContext;

    private TableAdapter mTableAdapter;
    private LayoutInflater mInflater;

    private Table mTable;

    @Nullable
    private OnValueClickListener mValueClickListener;

    public ValueAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setOnValueClickListener(OnValueClickListener listener) {
        this.mValueClickListener = listener;
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TableViewHolder(LayoutInflater.from(mContext).inflate(R.layout.widget_item_table, parent, false));
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        Table.Row row = mTable.getRows().get(position);
        List<Table.Value> values = row.getValues();

        holder.rowContainer.removeAllViews();
        for (int columnIndex = 0; columnIndex < mTableAdapter.getColumnCount(); columnIndex++) {
            View valueView = mTableAdapter.getValueView(mInflater, holder.rowContainer, columnIndex, position);

            LayoutParams params = valueView.getLayoutParams();
            params.width = mTable == null ? 0 : mTable.getColumnWidth(columnIndex);
            params.height = row.getHeight();
            holder.rowContainer.addView(valueView, params);

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
        return mTableAdapter == null || mTable == null ? 0 : mTable.getRows().size();
    }

    public void setTableAdapter(TableAdapter tableAdapter) {
        this.mTableAdapter = tableAdapter;
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
