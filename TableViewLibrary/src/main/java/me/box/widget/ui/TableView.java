/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.List;

import me.box.widget.R;
import me.box.widget.adapter.BaseTableAdapter;
import me.box.widget.adapter.TableAdapter;

/**
 * Created by box on 2017/6/29.
 * <p>
 * 表哥控件
 */

@SuppressWarnings({"WeakerAccess", "unused", "deprecation"})
public class TableView extends ContentFrameLayout {

    private NestedScrollView mRowScrollView;
    private HorizontalScrollView mContentScrollView;
    private LinearLayout mRowHeaderContainer;
    private LinearLayout mColumnHeaderContainer;
    private RecyclerView mValueContainer;

    private Space mSpacer;

    private ScrollHelper mScrollHelper;
    private LayoutInflater mInflater;

    @Nullable
    private TableAdapter mAdapter;
    private ValueAdapter mValueAdapter;

    private Drawable mColumnDivider;
    private Drawable mRowDivider;

    private AssembleTask mAssembleTask;
    @Nullable
    private AdapterDataSetObserver mDataSetObserver;

    @Nullable
    private OnColumnClickListener mColumnClicListener;
    @Nullable
    private OnRowClickListener mRowClickListener;

    public TableView(Context context) {
        this(context, null);
    }

    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableView, defStyleAttr, 0);

        mColumnDivider = getDrawable(a.getResourceId(R.styleable.TableView_columnHeaderDivider, 0));
        mRowDivider = getDrawable(a.getResourceId(R.styleable.TableView_rowHeaderDivider, 0));

        a.recycle();

        initializationLayout(context);

        if (!isInEditMode()) {
            return;
        }
        setAdapter(new PreviewAdapter());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        ViewGroup.LayoutParams layoutParams = mSpacer.getLayoutParams();
        layoutParams.height = mColumnHeaderContainer.getHeight();
        mSpacer.setLayoutParams(layoutParams);
    }

    public void setAdapter(@Nullable TableAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        this.mAdapter = adapter;
        if (mAdapter != null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }

        this.mValueAdapter.setTableAdapter(adapter);
        if (mAssembleTask != null) {
            mAssembleTask.cancel(true);
            mAssembleTask = null;
        }
        (mAssembleTask = new AssembleTask()).execute(adapter);
    }

    public void setOnColumnClickListener(OnColumnClickListener listener) {
        this.mColumnClicListener = listener;
    }

    public void setOnRowClickListener(OnRowClickListener listener) {
        this.mRowClickListener = listener;
    }

    public void setOnValueClickListener(final OnValueClickListener listener) {
        mValueAdapter.setOnValueClickListener((Table.Value value) -> {
            if (listener != null) {
                listener.onValueClick(TableView.this, value);
            }
        });
    }

    private void initializationLayout(Context context) {
        mInflater = LayoutInflater.from(context);

        LinearLayout contentView = (LinearLayout) mInflater.inflate(R.layout.widget_layout_table_view, this, false);
        addView(contentView);

        contentView.setDividerDrawable(mRowDivider);
        LinearLayout columnContainer = findViewById(R.id.widget_column_container);
        LinearLayout rowContainer = findViewById(R.id.widget_row_container);
        columnContainer.setDividerDrawable(mColumnDivider);
        rowContainer.setDividerDrawable(mColumnDivider);

        mRowScrollView = findViewById(R.id.widget_row_scroll);
        mContentScrollView = findViewById(R.id.widget_content_container);
        mRowHeaderContainer = findViewById(R.id.widget_row_header_container);
        mColumnHeaderContainer = findViewById(R.id.widget_column_header_container);
        mValueContainer = findViewById(R.id.widget_rv_value);
        mSpacer = findViewById(R.id.widget_spacer);

        mScrollHelper = new ScrollHelper(mValueContainer);

        mValueContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    mRowScrollView.scrollBy(dx, dy);
                }
            }
        });

        mRowScrollView.setOnTouchListener((view, motionEvent) -> {
            mValueContainer.onTouchEvent(motionEvent);
            return true;
        });

        mValueContainer.setAdapter(mValueAdapter = new ValueAdapter(context));
    }

    private void setRowNames(List<Table.Row> rows) {
        mScrollHelper.moveToPosition(0);
        mRowScrollView.scrollTo(0, 0);
        mRowHeaderContainer.removeAllViews();
        if (mAdapter == null) {
            return;
        }
        for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
            View itemView = mAdapter.getRowHeaderView(mInflater, mRowHeaderContainer, rowIndex);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mRowHeaderContainer.addView(itemView, layoutParams);

            Table.Row row = rows.get(rowIndex);

            itemView.setOnClickListener(view -> {
                if (mRowClickListener != null) {
                    mRowClickListener.onRowClick(this, row);
                }
            });
        }
    }

    private void setColumnNames(int columnCount) {
        mContentScrollView.scrollTo(0, 0);
        mColumnHeaderContainer.removeAllViews();
        if (mAdapter == null) {
            return;
        }
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            View itemView = mAdapter.getColumnHeaderView(mInflater, mColumnHeaderContainer, columnIndex);
            mColumnHeaderContainer.addView(itemView);

            int finalColumnIndex = columnIndex;
            itemView.setOnClickListener(view -> {
                if (mColumnClicListener != null) {
                    mColumnClicListener.onColumnClick(this, finalColumnIndex);
                }
            });
        }
    }

    private Drawable getDrawable(@DrawableRes int id) {
        if (id == 0) {
            return null;
        }
        Drawable imgOff = getResources().getDrawable(id);
        imgOff.setBounds(0, 0, imgOff.getMinimumWidth(), imgOff.getMinimumHeight());
        return imgOff;
    }

    private class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            setAdapter(mAdapter);
        }

        @Override
        public void onInvalidated() {
            setAdapter(mAdapter);
        }
    }

    private class AssembleTask extends AsyncTask<TableAdapter, Void, Table> {

        private Table table;

        @Override
        protected Table doInBackground(TableAdapter... adapters) {
            TableAdapter adapter = adapters[0];
            Table table = new Table();
            if (adapter == null) {
                return table;
            }
            int columnCount = adapter.getColumnCount();
            int rowCount = adapter.getRowCount();
            table.setColumnCount(columnCount);
            for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
                Table.Row row = new Table.Row();
                row.setRawRowIndex(rowIndex);
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    Table.Value value = new Table.Value();
                    value.setRawRowIndex(rowIndex);
                    value.setColumnIndex(columnIndex);
                    row.addValue(value);
                }
                table.addRow(row);
            }
            return table;
        }

        @Override
        protected void onPostExecute(final Table table) {
            this.table = table;

            setColumnNames(table.getColumnCount());
            setRowNames(table.getRows());

            ViewCompat.addOnceOnGlobalLayoutListener(mColumnHeaderContainer, new ColumnLayoutListener());
        }

        private class ColumnLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

            @Override
            public void onGlobalLayout() {
                int childCount = mColumnHeaderContainer.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    table.setColumnWidth(i, mColumnHeaderContainer.getChildAt(i).getWidth());
                }

                ViewCompat.addOnceOnGlobalLayoutListener(mRowHeaderContainer, new RowLayoutListener());
            }
        }

        private class RowLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {

            @Override
            public void onGlobalLayout() {
                List<Table.Row> rows = table.getRows();
                for (int i = 0; i < rows.size(); i++) {
                    rows.get(i).setHeight(mRowHeaderContainer.getChildAt(i).getHeight());
                }

                mValueAdapter.setTable(table);
            }
        }

    }

    public interface OnColumnClickListener {
        void onColumnClick(TableView view, int columnIndex);
    }

    public interface OnRowClickListener {
        void onRowClick(TableView view, Table.Row row);
    }

    public interface OnValueClickListener {
        void onValueClick(TableView view, Table.Value value);
    }

    private class PreviewAdapter extends BaseTableAdapter {

        private final int mPadding;

        public PreviewAdapter() {
            mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        }

        @Override
        public View getColumnHeaderView(LayoutInflater inflater, ViewGroup parent, int columnIndex) {
            return getView(String.format("Column%1$s", columnIndex));
        }

        @Override
        public View getRowHeaderView(LayoutInflater inflater, ViewGroup parent, int rowIndex) {
            return getView(String.format("Row%1$s", rowIndex));
        }

        @Override
        public View getValueView(LayoutInflater inflater, ViewGroup parent, int columnIndex, int rowIndex) {
            return getView(String.format("Value%1$s,%2$s", columnIndex, rowIndex));
        }

        @Override
        public int getColumnCount() {
            return 8;
        }

        @Override
        public int getRowCount() {
            return 20;
        }

        private View getView(CharSequence text) {
            TextView textView = new TextView(getContext());
            textView.setTextAppearance(getContext(), R.style.TextAppearance_AppCompat);
            textView.setText(text);
            textView.setSingleLine();
            textView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textView.setPadding(mPadding, mPadding, mPadding, mPadding);
            return textView;
        }
    }

}
