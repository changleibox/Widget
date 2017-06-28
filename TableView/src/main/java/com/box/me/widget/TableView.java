/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package com.box.me.widget;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Space;

import com.box.me.widget.adapter.RowNameAdapter;
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
public class TableView extends ContentFrameLayout implements View.OnTouchListener, RowNameAdapter.OnItemClickListener {

    private RecyclerView mValueView;
    private RecyclerView mRowNameView;
    private LinearLayout mColumnNameContainer;
    private HorizontalScrollView mHsvContainer;
    private Space mSpacer;

    private RowNameAdapter mRowNameAdapter;
    private TableAdapter mTableAdapter;

    private AssembleTask mAssembleTask;

    private boolean isReverse;

    @Nullable
    private View mTmpClickColumnView;

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
        mRowNameAdapter.setRows(sortedRows);

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

    @Override
    public void onItemClick(int position, Table.Row row) {
        if (mRowClickListener != null) {
            mRowClickListener.onRowClick(this, position, row);
        }
    }

    public void setOnSortListener(OnSortListener listener) {
        this.mSortListener = listener;
    }

    public void setOnRowClickListener(OnRowClickListener listener) {
        this.mRowClickListener = listener;
    }

    private void initializationLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_table_view, this, true);
        mValueView = findViewById(R.id.rv_value);
        mRowNameView = findViewById(R.id.rv_row_name);
        mColumnNameContainer = findViewById(R.id.item_row_container);
        mHsvContainer = findViewById(R.id.hsv_container);
        mSpacer = findViewById(R.id.spacer);

        mValueView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    mRowNameView.scrollBy(dx, dy);
                } else {
                    mTableAdapter.notifyDataSetChanged();
                }

                for (int i = 0; i < mRowNameAdapter.getItemCount(); i++) {
                    View view = mRowNameView.getChildAt(i);
                    Table.Row row = mRowNameAdapter.getRow(i);
                    if (row != null && view != null) {
                        row.setHeight(mRowNameView.getLayoutManager().getDecoratedMeasuredHeight(view));
                    }
                }
            }
        });

        mRowNameView.setOnTouchListener(this);

        mValueView.setAdapter(mTableAdapter = new TableAdapter(context));
        mRowNameView.setAdapter(mRowNameAdapter = new RowNameAdapter(getContext()));

        mRowNameAdapter.setOnItemClickListener(this);
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
            mHsvContainer.scrollTo(0, 0);
            mColumnNameContainer.removeAllViews();
            List<String> columnNames = table.getColumnNames();
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

            mColumnNameContainer.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        @Override
        public void onGlobalLayout() {
            mColumnNameContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            int childCount = mColumnNameContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                table.setColumnWidth(i, mColumnNameContainer.getChildAt(i).getWidth());
            }

            mRowNameAdapter.setRows(table.getRows());
            mRowNameAdapter.setHasAvatar(table.isHasAvatar());

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
