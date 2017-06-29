package me.box.widget.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import me.box.widget.R;

class TableViewHolder extends RecyclerView.ViewHolder {

    ViewGroup rowContainer;

    TableViewHolder(View itemView) {
        super(itemView);
        rowContainer = itemView.findViewById(R.id.item_row_container);
    }
}