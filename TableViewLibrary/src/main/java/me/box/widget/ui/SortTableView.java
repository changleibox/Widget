/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.List;

import me.box.widget.impl.SortAdapter;

/**
 * Created by Box on 2017/7/2.
 * <p>
 * 具有按照列排序功能的tableView
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class SortTableView extends TableView {

    public enum SortType {
        Order, Reverse
    }

    private SortTask mSortTask;

    private SortType mDefaultSortType = SortType.Order;

    private boolean isReverse;

    @Nullable
    private OnSortListener mSortListener;
    @Nullable
    private SortAdapter mSortAdapter;

    public SortTableView(Context context) {
        super(context);
    }

    public SortTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SortTableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    void onColumnClick(TableView view, int columnIndex) {
        if (mSortTask != null) {
            mSortTask.cancel(true);
            mSortTask = null;
        }
        (mSortTask = new SortTask(columnIndex)).execute();
    }

    public void setAdapter(@Nullable SortAdapter adapter) {
        this.mSortAdapter = adapter;
        super.setAdapter(adapter);
    }

    public void setDefaultSortType(SortType sortType) {
        this.mDefaultSortType = sortType;
        performClickColumn(0);
    }

    public void setOnSortListener(OnSortListener listener) {
        this.mSortListener = listener;
    }

    private class SortTask extends AsyncTask<Void, Void, List<?>> {

        private SortType mSortType;
        private final int column;

        public SortTask(int column) {
            this.column = column;
        }

        @Override
        protected List<?> doInBackground(Void... voids) {
            if (mSortAdapter == null) {
                return null;
            }
            List<?> sortedRows;
            if (isReverse) {
                mSortType = mDefaultSortType == SortType.Order ? SortType.Reverse : SortType.Order;
                sortedRows = mSortAdapter.reverse();
            } else {
                mSortType = mDefaultSortType;
                isReverse = true;
                sortedRows = mSortAdapter.sort(mDefaultSortType == SortType.Order, column);
            }
            return sortedRows;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void onPostExecute(List<?> rows) {
            if (rows == null || mSortAdapter == null) {
                return;
            }
            mSortAdapter.setRows(rows);
            mSortAdapter.notifyDataSetChanged();

            if (mSortListener != null) {
                mSortListener.onSort(SortTableView.this, column, mSortType, rows);
            }
        }
    }

    public interface OnSortListener {
        void onSort(TableView view, int columnIndex, SortType sortType, List<?> sortedRows);
    }
}
