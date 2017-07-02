/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.app.testtableview;

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

import java.util.List;

import me.box.app.testtableview.activity.BaseActivity;
import me.box.app.testtableview.entity.Table;
import me.box.widget.adapter.ArrayAdapter;
import me.box.widget.ui.TableView;

/**
 * Created by box on 2017/6/29.
 * <p>
 * 测试TableView
 */

public class TestTableViewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int ROW_COUNT = 15;
    private static final int COLUMNS_COUNT = 11;

    private TableView mTableView;
    private SwipeRefreshLayout mRefreshLayout;

    private TestSortTask mTestSortTask;

    private TestAdapter mTableAdapter;

    @Override
    public void onInitViews(@Nullable Bundle savedInstanceState) {
        mTableView = (TableView) findViewById(R.id.vh_recycler_view);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mTableAdapter = new TestAdapter();
    }

    @Override
    public void onInitDatas(@Nullable Bundle savedInstanceState) {
        mTableView.setAdapter(mTableAdapter);
        onRefresh();
    }

    @Override
    public void onInitListeners(@Nullable Bundle savedInstanceState) {
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Nullable
    @Override
    public View getLayout(@Nullable Bundle savedInstanceState, @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return inflater.inflate(R.layout.activity_test_table_view, parent, false);
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

    private class TestSortTask extends AsyncTask<Void, Void, Table> {

        @Override
        protected Table doInBackground(Void... voids) {
            Table table = new Table();
            table.setHasAvatar(true);
            for (int column = 0; column < COLUMNS_COUNT; column++) {
                table.addColumnName(String.format("Column%1$s", column));
            }
            for (int row = 0; row < ROW_COUNT; row++) {
                Table.Row item = new Table.Row("Row" + row);
                for (int column = 0; column < COLUMNS_COUNT; column++) {
                    item.addValue(new Table.Value(Math.random()));
                }
                table.addRow(item);
            }
            return table;
        }

        @Override
        protected void onPostExecute(final Table table) {
            mRefreshLayout.setRefreshing(false);
            mTableAdapter.setTable(table);
            mTableAdapter.notifyDataSetChanged();
        }
    }

    private class TestAdapter extends ArrayAdapter<String, Table.Row, Table.Value> {

        private boolean hasAvatar;

        private void setTable(Table table) {
            if (table == null) {
                return;
            }
            hasAvatar = table.isHasAvatar();
            setNotifyOnChange(false);
            List<Table.Row> rows = table.getRows();
            setColumns(table.getColumnNames());
            setRows(rows);
            for (int i = 0; i < rows.size(); i++) {
                setValuesToRow(i, rows.get(i).getValues());
            }
            notifyDataSetChanged();
        }

        @Override
        protected View getColumnHeaderView(LayoutInflater inflater, ViewGroup parent, String s, int columnIndex) {
            TableValueView valueView = new TableValueView(TestTableViewActivity.this);
            valueView.setText(s);
            valueView.setMinHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics()));
            return valueView;
        }

        @Override
        protected View getRowHeaderView(LayoutInflater inflater, ViewGroup parent, Table.Row row, int rowIndex) {
            View itemView = inflater.inflate(R.layout.layout_row_name, parent, false);
            TableValueView rowNameView = itemView.findViewById(R.id.tv_name);
            ImageView ivAvatar = itemView.findViewById(R.id.iv_avatar);

            ivAvatar.setVisibility(hasAvatar ? View.VISIBLE : View.GONE);
            ivAvatar.setImageResource(R.mipmap.ic_launcher_round);
            rowNameView.setText(row.getRowName());
            return itemView;
        }

        @Override
        protected View getValueView(LayoutInflater inflater, ViewGroup parent, @Nullable Table.Value value, int columnIndex, int rowIndex) {
            // List<Table.Row> rows = mTable.getRows();
            // if (rows.size() <= rowIndex) {
            //     return null;
            // }
            // List<Table.Value> values = rows.get(rowIndex).getValues();
            // if (values.size() <= columnIndex) {
            //     return null;
            // }
            if (value == null) {
                return null;
            }
            View itemView = inflater.inflate(R.layout.item_table_value, parent, false);
            TextView tvValue = itemView.findViewById(R.id.tv_value);
            tvValue.setText(value.getLabel());
            return itemView;
        }
    }

}
