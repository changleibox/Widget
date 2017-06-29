/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.adapter;

import android.database.DataSetObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by box on 2017/6/29.
 * <p>
 * tableView的适配器
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class BaseTableAdapter implements TableAdapter {

    private final List<DataSetObserver> mDataSetObservers = new ArrayList<>();

    public BaseTableAdapter() {
    }

    public void notifyDataSetChanged() {
        for (int i = 0; i < mDataSetObservers.size(); i++) {
            DataSetObserver dataSetObserver = mDataSetObservers.get(i);
            if (dataSetObserver == null) {
                continue;
            }
            dataSetObserver.onChanged();
        }
    }

    public void notifyDataSetInvalidated() {
        for (int i = 0; i < mDataSetObservers.size(); i++) {
            DataSetObserver dataSetObserver = mDataSetObservers.get(i);
            if (dataSetObserver == null) {
                continue;
            }
            dataSetObserver.onInvalidated();
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver var1) {
        mDataSetObservers.add(var1);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver var1) {
        mDataSetObservers.remove(var1);
    }
}
