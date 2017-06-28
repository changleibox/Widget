package com.box.me.widget.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.box.me.widget.R;
import com.box.me.widget.adapter.viewholder.TableViewHolder;
import com.box.me.widget.entity.Table;
import com.box.me.widget.view.TableValueView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class RowNameAdapter extends RecyclerView.Adapter<TableViewHolder> {

    private final Context mContext;
    private final List<Table.Row> mRows;
    private final LayoutInflater mInflater;
    private boolean hasAvatar;
    @Nullable
    private OnItemClickListener mItemClickListener;

    public RowNameAdapter(Context context) {
        this.mContext = context;
        this.mRows = new ArrayList<>();
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public TableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TableViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_table, parent, false));
    }

    @Override
    public void onBindViewHolder(TableViewHolder holder, int position) {
        Table.Row row = mRows.get(position);

        View itemView = mInflater.inflate(R.layout.layout_row_name, holder.rowContainer, false);
        TableValueView rowNameView = itemView.findViewById(R.id.tv_name);
        ImageView ivAvatar = itemView.findViewById(R.id.iv_avatar);

        ivAvatar.setVisibility(hasAvatar ? View.VISIBLE : View.GONE);
        ivAvatar.setImageResource(R.mipmap.ic_launcher_round);
        rowNameView.setText(row.getRowName());

        holder.rowContainer.removeAllViews();
        holder.rowContainer.addView(itemView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        LayoutParams itemLayoutParams = holder.itemView.getLayoutParams();
        itemLayoutParams.width = LayoutParams.MATCH_PARENT;
        holder.itemView.setLayoutParams(itemLayoutParams);

        if (mItemClickListener != null) {
            mItemClickListener.onItemClick(position, row);
        }
    }

    @Override
    public int getItemCount() {
        return mRows.size();
    }

    @Nullable
    public Table.Row getRow(int position) {
        return position >= mRows.size() ? null : mRows.get(position);
    }

    public void setRows(List<Table.Row> rows) {
        mRows.clear();
        mRows.addAll(rows);
        notifyDataSetChanged();
    }

    public void setHasAvatar(boolean hasAvatar) {
        this.hasAvatar = hasAvatar;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Table.Row row);
    }

}