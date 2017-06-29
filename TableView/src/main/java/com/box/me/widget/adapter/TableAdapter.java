package com.box.me.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.box.me.widget.R;
import com.box.me.widget.adapter.viewholder.TableViewHolder;
import com.box.me.widget.entity.Table;
import com.box.me.widget.view.TableValueView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by box on 2017/6/26.
 * <p>
 * 测试
 */

@SuppressWarnings({"deprecation", "WeakerAccess"})
public class TableAdapter extends RecyclerView.Adapter<TableViewHolder> {

    private final List<Table.Row> mRows = new ArrayList<>();
    private final List<String> mColumnNames = new ArrayList<>();
    private final Context mContext;

    private Table mTable;

    public TableAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TableViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_table, parent, false));
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        holder.rowContainer.removeAllViews();
        Table.Row row = mRows.get(position);
        List<Table.Value> values = row.getValues();
        for (int i = 0; i < mColumnNames.size(); i++) {
            TableValueView valueView = new TableValueView(mContext,
                    i >= values.size() ? new Table.Value(0.0d) : values.get(i));
            LayoutParams params = valueView.getLayoutParams();
            params.width = mTable == null ? 0 : mTable.getColumnWidth(i);
            params.height = LayoutParams.MATCH_PARENT;
            holder.rowContainer.addView(valueView, params);
        }

        LayoutParams rowLayoutParams = holder.rowContainer.getLayoutParams();
        if (rowLayoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) rowLayoutParams).weight = 1;
        }
        holder.rowContainer.setLayoutParams(rowLayoutParams);

        LayoutParams itemLayoutParams = holder.itemView.getLayoutParams();
        itemLayoutParams.height = row.getHeight();
        holder.itemView.setLayoutParams(itemLayoutParams);
    }

    @Override
    public int getItemCount() {
        return mRows.size();
    }

    public void setTable(Table table) {
        mTable = table;
        mRows.clear();
        mColumnNames.clear();
        if (table != null) {
            mRows.addAll(table.getRows());
            mColumnNames.addAll(table.getColumnNames());
        }
        notifyDataSetChanged();
    }

    public List<Table.Row> sort(Comparator<Table.Row> comparator) {
        Collections.sort(mRows, comparator);
        notifyDataSetChanged();
        return Collections.unmodifiableList(mRows);
    }

    public List<Table.Row> sort(final boolean isOrder, final int column) {
        return sort(new Comparator<Table.Row>() {
            @Override
            public int compare(Table.Row row1, Table.Row row2) {
                double value1 = row1.getValues().get(column).getValue();
                double value2 = row2.getValues().get(column).getValue();
                return isOrder ? Double.compare(value1, value2) : Double.compare(value2, value1);
            }
        });
    }

    public List<Table.Row> reverse() {
        Collections.reverse(mRows);
        notifyDataSetChanged();
        return Collections.unmodifiableList(mRows);
    }

}
