/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.adapter;

import android.database.DataSetObserver;

/**
 * Created by box on 2017/6/29.
 * <p>
 * tableView的适配器
 */

public abstract class BaseTableAdapter implements TableAdapter {

    public BaseTableAdapter() {
    }

    public void notifyDataSetChanged() {
    }

    public void notifyDataSetInvalidated() {
    }

    @Override
    public void registerDataSetObserver(DataSetObserver var1) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver var1) {

    }
}
