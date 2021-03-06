/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.app.tableviewdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.box.app.tableviewdemo.activity.BaseActivity;
import me.box.app.tableviewdemo.entity.TableData;
import me.box.widget.impl.SortAdapter;
import me.box.widget.ui.SortTableView;
import me.box.widget.ui.SortTableView.SortType;
import me.box.widget.ui.Table;
import me.box.widget.ui.TableView;

/**
 * Created by box on 2017/6/29.
 * <p>
 * 测试TableView
 */

public class TableViewDemoActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int ROW_COUNT = 15;
    private static final int COLUMNS_COUNT = 11;

    private SortTableView mTableView;
    private SwipeRefreshLayout mRefreshLayout;

    private TestSortTask mTestSortTask;

    private TestAdapter mTableAdapter;

    private SortType mSortType;
    private int mSortColumnIndex;

    @Override
    public void onInitViews(@Nullable Bundle savedInstanceState) {
        mTableView = (SortTableView) findViewById(R.id.vh_recycler_view);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mTableAdapter = new TestAdapter();
    }

    @Override
    public void onInitDatas(@Nullable Bundle savedInstanceState) {
        mTableView.setAdapter(mTableAdapter);
        mTableView.setDefaultSortType(SortType.Order);
        onRefresh();
    }

    @Override
    public void onInitListeners(@Nullable Bundle savedInstanceState) {
        mRefreshLayout.setOnRefreshListener(this);
        mTableView.setOnSortListener(new SortTableView.OnSortListener() {
            @Override
            public void onSort(TableView view, int columnIndex, SortType sortType) {
                mSortType = sortType;
                mSortColumnIndex = columnIndex;
                mTableAdapter.notifyDataSetChanged();
            }
        });
        mTableView.setOnColumnClickListener(new TableView.OnColumnClickListener() {
            @Override
            public void onColumnClick(TableView view, int columnIndex) {
                Toast.makeText(getApplicationContext(), String.format("点击%1$s", columnIndex), Toast.LENGTH_SHORT).show();
            }
        });
        mTableView.setOnRowClickListener(new TableView.OnRowClickListener() {
            @Override
            public void onRowClick(TableView view, Table.Row row) {
                Toast.makeText(getApplicationContext(), String.format("点击%1$s", row.getRawRowIndex()), Toast.LENGTH_SHORT).show();
            }
        });
        mTableView.setOnValueClickListener(new TableView.OnValueClickListener() {
            @Override
            public void onValueClick(TableView view, Table.Value value) {
                Toast.makeText(getApplicationContext(), String.format("点击%1$s-%2$s", value.getColumnIndex(), value.getRawRowIndex()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Nullable
    @Override
    public View getLayout(@Nullable Bundle savedInstanceState, @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return inflater.inflate(R.layout.activity_table_view_demo, parent, false);
    }

    @Override
    protected void onDestroy() {
        if (mTestSortTask != null) {
            mTestSortTask.cancel(true);
            mTestSortTask = null;
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        if (mTestSortTask != null) {
            mTestSortTask.cancel(true);
            mTestSortTask = null;
        }
        (mTestSortTask = new TestSortTask()).execute();
    }

    private class TestSortTask extends AsyncTask<Void, Void, TableData> {

        @Override
        protected TableData doInBackground(Void... voids) {
            TableData table = new TableData();
            table.setHasAvatar(true);
            for (int column = 0; column < COLUMNS_COUNT; column++) {
                table.addColumnName(String.format("Column%1$s", column));
            }
            for (int row = 0; row < ROW_COUNT; row++) {
                TableData.Row item = new TableData.Row("Row" + row);
                item.setCurrentRowIndex(row);
                for (int column = 0; column < COLUMNS_COUNT; column++) {
                    item.addValue(new TableData.Value(Math.random()));
                }
                table.addRow(item);
            }
            return table;
        }

        @Override
        protected void onPostExecute(final TableData table) {
            mRefreshLayout.setRefreshing(false);
            mTableAdapter.setTable(table);
            mTableAdapter.notifyDataSetInvalidated();
            mTableView.resetPerFormClickColumn(0);
        }
    }

    private class TestAdapter extends SortAdapter<String, TableData.Row, TableData.Value> {

        private boolean hasAvatar;
        private List<TableData.Row> mRows;

        private void setTable(TableData table) {
            if (table == null) {
                return;
            }
            hasAvatar = table.isHasAvatar();
            setNotifyOnChange(false);
            mRows = table.getRows();
            setColumns(table.getColumnNames());
            setRows(mRows);
            for (int i = 0; i < mRows.size(); i++) {
                setValuesToRow(i, mRows.get(i).getValues());
            }
            notifyDataSetChanged();
        }

        @Override
        protected View getColumnHeaderView(LayoutInflater inflater, ViewGroup parent, String s, int columnIndex) {
            TableValueView valueView = new TableValueView(TableViewDemoActivity.this);
            valueView.setText(s);
            if (columnIndex == mSortColumnIndex) {
                valueView.append(mSortType == null || mSortType == SortType.Order ? "-顺序" : "-倒序");
            }
            valueView.setMinHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()));
            return valueView;
        }

        @Override
        protected View getRowHeaderView(LayoutInflater inflater, ViewGroup parent, TableData.Row row, int rowIndex) {
            View itemView = inflater.inflate(R.layout.layout_row_name, parent, false);
            TableValueView rowNameView = itemView.findViewById(R.id.tv_name);
            ImageView ivAvatar = itemView.findViewById(R.id.iv_avatar);

            ivAvatar.setVisibility(hasAvatar ? View.VISIBLE : View.GONE);
            ivAvatar.setImageResource(R.mipmap.ic_launcher_round);
            rowNameView.setText(row.getRowName());
            return itemView;
        }

        @Override
        protected View getValueView(LayoutInflater inflater, ViewGroup parent, @Nullable TableData.Value value, int columnIndex, int rowIndex) {
            if (value == null) {
                return null;
            }
            View itemView = inflater.inflate(R.layout.item_table_value, parent, false);
            TextView tvValue = itemView.findViewById(R.id.tv_value);
            tvValue.setText(value.getLabel());
            return itemView;
        }

        @Override
        public void sort(final boolean isOrder, final int columnIndex) {
            if (mRows == null || mRows.isEmpty()) {
                return;
            }
            Collections.sort(mRows, new Comparator<TableData.Row>() {
                @Override
                public int compare(TableData.Row row1, TableData.Row row2) {
                    List<TableData.Value> values1 = row1.getValues();
                    List<TableData.Value> values2 = row2.getValues();
                    if (values1.size() <= columnIndex || values2.size() <= columnIndex) {
                        return 0;
                    }
                    double value1 = values1.get(columnIndex).getValue();
                    double value2 = values2.get(columnIndex).getValue();
                    return isOrder ? Double.compare(value1, value2) : Double.compare(value2, value1);
                }
            });
            setNotifyOnChange(false);
            setRows(mRows);
            for (int i = 0; i < mRows.size(); i++) {
                setValuesToRow(i, mRows.get(i).getValues());
            }
            setNotifyOnChange(true);
        }

        @Override
        public void reverse() {
            if (mRows == null || mRows.isEmpty()) {
                return;
            }
            Collections.reverse(mRows);
            setNotifyOnChange(false);
            setRows(mRows);
            for (int i = 0; i < mRows.size(); i++) {
                setValuesToRow(i, mRows.get(i).getValues());
            }
            setNotifyOnChange(true);
        }
    }

}
