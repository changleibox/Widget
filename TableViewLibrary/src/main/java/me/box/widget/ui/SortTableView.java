/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

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

    private int mTmpClickColumnIndex = -1;

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
        if (mTmpClickColumnIndex != columnIndex) {
            isReverse = false;
            mTmpClickColumnIndex = columnIndex;
        }
        if (mSortTask != null) {
            mSortTask.cancel(true);
            mSortTask = null;
        }
        (mSortTask = new SortTask(columnIndex)).execute();
    }

    public void resetPerFormClickColumn(int columnIndex) {
        isReverse = false;
        performClickColumn(columnIndex);
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

    private class SortTask extends AsyncTask<Void, Void, Void> {

        private SortType mSortType;
        private final int columnIndex;

        public SortTask(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mSortAdapter == null) {
                return null;
            }
            if (isReverse) {
                mSortType = mDefaultSortType == SortType.Order ? SortType.Reverse : SortType.Order;
                mSortAdapter.reverse();
            } else {
                mSortType = mDefaultSortType;
                isReverse = true;
                mSortAdapter.sort(mDefaultSortType == SortType.Order, columnIndex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mSortAdapter == null) {
                return;
            }

            refreshRowNames(() -> {
                refreshRowHeight();
                refreshValues();
            });

            if (mSortListener != null) {
                mSortListener.onSort(SortTableView.this, columnIndex, mSortType);
            }
        }
    }

    public interface OnSortListener {
        void onSort(TableView view, int columnIndex, SortType sortType);
    }
}
