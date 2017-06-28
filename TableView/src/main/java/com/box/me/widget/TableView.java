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
import com.box.me.widget.entity.Table;
import com.box.me.widget.view.TableValueView;

import java.util.List;

/**
 * Created by Box on 2017/6/27.
 * <p>
 * 表格控件，可上下左右滑动
 */

@SuppressWarnings({"WeakerAccess", "deprecation"})
public class TableView extends ContentFrameLayout implements View.OnTouchListener {

    private RecyclerView mValueView;
    private LinearLayout mColumnNameContainer;
    private HorizontalScrollView mHsvContainer;
    private Space mSpacer;
    private LinearLayout mRowNameContainer;
    private NestedScrollView mRowScrollView;

    private TableAdapter mTableAdapter;

    private AssembleTask mAssembleTask;

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

    public void performClickColumn(int column) {
        View view = mColumnNameContainer.getChildAt(column);
        if (mTmpClickColumnView != view) {
            isReverse = false;
            mTmpClickColumnView = view;
        }
        List<Table.Row> sortedRows;
        if (isReverse) {
            sortedRows = mTableAdapter.reverse();
        } else {
            isReverse = true;
            sortedRows = mTableAdapter.sort(column);
        }
        setRowNames(sortedRows, mTable != null && mTable.isHasAvatar());

        if (mSortListener != null) {
            mSortListener.onSort(this, column, sortedRows);
        }
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

            final int finalRowIndex = rowIndex;
            itemRowName.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mRowClickListener != null) {
                        mRowClickListener.onRowClick(TableView.this, finalRowIndex, row);
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

    private class AssembleTask extends AsyncTask<Table, Void, Table> implements OnGlobalLayoutListener {

        private Table table;

        @Override
        protected Table doInBackground(Table... tables) {
            Table table = tables[0];
            final List<Table.Row> rows = table.getRows();
            for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
                Table.Row row = rows.get(rowIndex);
                row.setRow(rowIndex);
                List<Table.Value> values = row.getValues();
                for (int columnIndex = 0; columnIndex < values.size(); columnIndex++) {
                    Table.Value value = values.get(columnIndex);
                    value.setRow(rowIndex);
                    value.setColumn(columnIndex);
                }
            }
            return table;
        }

        @Override
        protected void onPostExecute(final Table table) {
            this.table = table;

            setColumnNames(table.getColumnNames());
            setRowNames(table.getRows(), table.isHasAvatar());

            mColumnNameContainer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mColumnNameContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                    int childCount = mColumnNameContainer.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        table.setColumnWidth(i, mColumnNameContainer.getChildAt(i).getWidth());
                    }

                    mRowNameContainer.getViewTreeObserver().addOnGlobalLayoutListener(AssembleTask.this);
                }
            });
        }

        @Override
        public void onGlobalLayout() {
            mRowNameContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            List<Table.Row> rows = table.getRows();
            for (int i = 0; i < rows.size(); i++) {
                rows.get(i).setHeight(mRowNameContainer.getChildAt(i).getHeight());
            }

            mTableAdapter.setTable(table);

            resetPerformClickColumn(0);
        }

    }

    public interface OnSortListener {
        void onSort(TableView view, int columnIndex, List<Table.Row> sortedRows);
    }

    public interface OnRowClickListener {
        void onRowClick(TableView view, int rowIndex, Table.Row row);
    }
}
