package com.box.me.widget.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.box.me.widget.R;

public class TableViewHolder extends RecyclerView.ViewHolder {

    public ViewGroup rowContainer;

    public TableViewHolder(View itemView) {
        super(itemView);
        rowContainer = itemView.findViewById(R.id.item_row_container);
    }
}