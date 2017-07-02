/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.impl;

import java.util.List;

import me.box.widget.adapter.ArrayAdapter;

/**
 * Created by Box on 2017/7/2.
 * <p>
 * 排序
 */

public abstract class SortAdapter<Column, Row, Value> extends ArrayAdapter<Column, Row, Value> {

    public abstract List<Row> sort(final boolean isOrder, final int column);

    public abstract List<Row> reverse();
}
