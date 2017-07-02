/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.impl;

import me.box.widget.adapter.TableAdapter;

/**
 * Created by Box on 2017/7/2.
 * <p>
 * 排序
 */

public interface SortAdapter extends TableAdapter {

    int[] sort(final boolean isOrder, final int column);

    int[] reverse();
}
