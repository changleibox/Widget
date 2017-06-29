/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.box.me.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;

import com.box.me.widget.adapter.TableAdapter;
import com.box.me.widget.compat.ViewCompat;
import com.box.me.widget.entity.Table;
import com.box.me.widget.helper.ScrollHelper;
import com.box.me.widget.ui.TableValueView;

import java.util.List;

/**
 * Created by Box on 2017/6/27.
 * <p>
 * 表格控件，可上下左右滑动
 */

@SuppressWarnings({"WeakerAccess", "deprecation"})
public class TableView extends ContentFrameLayout implements View.OnTouchListener {

    public enum SortType {
        Order, Reverse
    }

    private RecyclerView mValueView;
    private LinearLayout mColumnNameContainer;
    private HorizontalScrollView mHsvContainer;
    private Space mSpacer;
    private LinearLayout mRowNameContainer;
    private NestedScrollView mRowScrollView;

    private TableAdapter mTableAdapter;

    private AssembleTask mAssembleTask;
    private SortTask mSortTask;

    private boolean isReverse;

    @Nullable
    private View mTmpClickColumnView;

    private LayoutInflater mInflater;

    @Nullable
    private Table mTable;

    @Nullable
    private OnSortListener mSortListener;
    @Nullable
    private OnRowClickListener mRowClickListener;

    private SortType mDefaultSortType = SortType.Order;

    public TableView(@NonNull Context context) {
        this(context, null);
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializationLayout(context);
    }

    public void setTable(@NonNull final Table table) {
        mTable = table;
        if (mAssembleTask != null) {
            mAssembleTask.cancel(true);
            mAssembleTask = null;
        }
        (mAssembleTask = new AssembleTask()).execute(table);
    }

    public void setDefaultSortType(SortType sortType) {
        this.mDefaultSortType = sortType;
        resetPerformClickColumn(0);
    }

    public void performClickColumn(int column) {
        if (mSortTask != null) {
            mSortTask.cancel(true);
            mSortTask = null;
        }
        int childCount = mColumnNameContainer.getChildCount();
        if (mTable == null || childCount <= column) {
            return;
        }
        View view = mColumnNameContainer.getChildAt(column);
        if (mTmpClickColumnView != view) {
            isReverse = false;
            mTmpClickColumnView = view;
        }
        // SortType sortType;
        // List<Table.Row> sortedRows;
        // if (isReverse) {
        //     sortType = mDefaultSortType == SortType.Order ? SortType.Reverse : SortType.Order;
        //     sortedRows = mTableAdapter.reverse();
        // } else {
        //     sortType = mDefaultSortType;
        //     isReverse = true;
        //     sortedRows = mTableAdapter.sort(mDefaultSortType == SortType.Order, column);
        // }
        // setRowNames(sortedRows, mTable != null && mTable.isHasAvatar());
        //
        // if (mSortListener != null) {
        //     mSortListener.onSort(this, column, sortType, sortedRows);
        // }
        (mSortTask = new SortTask(column)).execute();
    }

    public void resetPerformClickColumn(int column) {
        isReverse = false;
        performClickColumn(column);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ViewGroup.LayoutParams layoutParams = mSpacer.getLayoutParams();
        layoutParams.height = mColumnNameContainer.getHeight();
        mSpacer.setLayoutParams(layoutParams);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mValueView.onTouchEvent(motionEvent);
        return true;
    }

    public void setOnSortListener(OnSortListener listener) {
        this.mSortListener = listener;
    }

    public void setOnRowClickListener(OnRowClickListener listener) {
        this.mRowClickListener = listener;
    }

    public void setOnValueClickListener(final OnValueClickListener listener) {
        mTableAdapter.setOnValueClickListener(new TableAdapter.OnValueClickListener() {
            @Override
            public void onValueClick(Table.Value value) {
                if (listener != null) {
                    listener.onValueClick(TableView.this, value);
                }
            }
        });
    }

    private void initializationLayout(Context context) {
        mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.layout_table_view, this, true);
        mValueView = findViewById(R.id.rv_value);
        mColumnNameContainer = findViewById(R.id.item_row_container);
        mHsvContainer = findViewById(R.id.hsv_container);
        mSpacer = findViewById(R.id.spacer);
        mRowNameContainer = findViewById(R.id.row_name_container);
        mRowScrollView = findViewById(R.id.row_scroll);

        mValueView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    mRowScrollView.scrollBy(dx, dy);
                }
            }
        });

        mRowScrollView.setOnTouchListener(this);

        mValueView.setAdapter(mTableAdapter = new TableAdapter(context));
    }

    private void setRowNames(List<Table.Row> rows, boolean hasAvatar) {
        mRowScrollView.scrollTo(0, 0);
        new ScrollHelper(mValueView).moveToPosition(0);
        mRowNameContainer.removeAllViews();
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            View itemRowName = mInflater.inflate(R.layout.item_table, mRowNameContainer, false);
            LinearLayout rowContainer = itemRowName.findViewById(R.id.item_row_container);

            final Table.Row row = rows.get(rowIndex);

            View itemView = mInflater.inflate(R.layout.layout_row_name, rowContainer, false);
            TableValueView rowNameView = itemView.findViewById(R.id.tv_name);
            ImageView ivAvatar = itemView.findViewById(R.id.iv_avatar);

            ivAvatar.setVisibility(hasAvatar ? View.VISIBLE : View.GONE);
            ivAvatar.setImageResource(R.mipmap.ic_launcher_round);
            rowNameView.setText(row.getRowName());

            rowContainer.removeAllViews();
            rowContainer.addView(itemView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            mRowNameContainer.addView(itemRowName, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

            itemRowName.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mRowClickListener != null) {
                        mRowClickListener.onRowClick(TableView.this, row);
                    }
                }
            });
        }
    }

    private void setColumnNames(List<String> columnNames) {
        mHsvContainer.scrollTo(0, 0);
        mColumnNameContainer.removeAllViews();
        for (int columnIndex = 0; columnIndex < columnNames.size(); columnIndex++) {
            TableValueView valueView = new TableValueView(getContext());
            valueView.setText(columnNames.get(columnIndex));
            mColumnNameContainer.addView(valueView);

            final int tmpColumnIndex = columnIndex;
            valueView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    performClickColumn(tmpColumnIndex);
                }
            });
        }
    }

    private class AssembleTask extends AsyncTask<Table, Void, Table> {

        private Table table;

        @Override
        protected Table doInBackground(Table... tables) {
            Table table = tables[0];
            final List<Table.Row> rows = table.getRows();
            for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                Table.Row row = rows.get(rowIndex);
                row.setRawRowIndex(rowIndex);
                List<Table.Value> values = row.getValues();
                for (int columnIndex = 0; columnIndex < values.size(); columnIndex++) {
                    Table.Value value = values.get(columnIndex);
                    value.setRawRowIndex(rowIndex);
                    value.setColumnIndex(columnIndex);
                }
            }
            return table;
        }

        @Override
        protected void onPostExecute(final Table table) {
            this.table = table;

            setColumnNames(table.getColumnNames());
            setRowNames(table.getRows(), table.isHasAvatar());

            ViewCompat.addOnceOnGlobalLayoutListener(mColumnNameContainer, new ColumnLayoutListener());
        }

        private class ColumnLayoutListener implements OnGlobalLayoutListener {

            @Override
            public void onGlobalLayout() {
                int childCount = mColumnNameContainer.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    table.setColumnWidth(i, mColumnNameContainer.getChildAt(i).getWidth());
                }

                ViewCompat.addOnceOnGlobalLayoutListener(mRowNameContainer, new RowLayoutListener());
            }
        }

        private class RowLayoutListener implements OnGlobalLayoutListener {

            @Override
            public void onGlobalLayout() {
                List<Table.Row> rows = table.getRows();
                for (int i = 0; i < rows.size(); i++) {
                    rows.get(i).setHeight(mRowNameContainer.getChildAt(i).getHeight());
                }

                mTableAdapter.setTable(table);

                resetPerformClickColumn(0);
            }
        }

    }

    private class SortTask extends AsyncTask<Void, Void, List<Table.Row>> {

        private SortType mSortType;
        private final int column;

        public SortTask(int column) {
            this.column = column;
        }

        @Override
        protected List<Table.Row> doInBackground(Void... voids) {
            List<Table.Row> sortedRows;
            if (isReverse) {
                mSortType = mDefaultSortType == SortType.Order ? SortType.Reverse : SortType.Order;
                sortedRows = mTableAdapter.reverse();
            } else {
                mSortType = mDefaultSortType;
                isReverse = true;
                sortedRows = mTableAdapter.sort(mDefaultSortType == SortType.Order, column);
            }
            for (int rowIndex = 0; rowIndex < sortedRows.size(); rowIndex++) {
                Table.Row row = sortedRows.get(rowIndex);
                row.setCurrentRowIndex(rowIndex);
                List<Table.Value> values = row.getValues();
                for (Table.Value value : values) {
                    value.setCurrentRowIndex(rowIndex);
                }
            }
            return sortedRows;
        }

        @Override
        protected void onPostExecute(List<Table.Row> rows) {
            mTableAdapter.notifyDataSetChanged();

            setRowNames(rows, mTable != null && mTable.isHasAvatar());

            if (mSortListener != null) {
                mSortListener.onSort(TableView.this, column, mSortType, rows);
            }
        }
    }

    public interface OnSortListener {
        void onSort(TableView view, int columnIndex, SortType sortType, List<Table.Row> sortedRows);
    }

    public interface OnRowClickListener {
        void onRowClick(TableView view, Table.Row row);
    }

    public interface OnValueClickListener {
        void onValueClick(TableView view, Table.Value value);
    }
}
