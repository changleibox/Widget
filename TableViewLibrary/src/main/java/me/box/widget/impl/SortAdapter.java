/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.impl;

import android.support.annotation.WorkerThread;

import me.box.widget.adapter.ArrayAdapter;

/**
 * Created by Box on 2017/7/2.
 * <p>
 * 排序
 */

public abstract class SortAdapter<Column, Row, Value> extends ArrayAdapter<Column, Row, Value> {

    @WorkerThread
    public abstract void sort(final boolean isOrder, final int columnIndex);

    @WorkerThread
    public abstract void reverse();
}
