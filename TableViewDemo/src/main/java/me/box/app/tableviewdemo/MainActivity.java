/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.app.tableviewdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.box.app.tableviewdemo.activity.BaseActivity;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    // private static final int ROW_COUNT = 15;
    // private static final int COLUMNS_COUNT = 8;
    //
    // private TableView mRecyclerView;
    // private SwipeRefreshLayout mRefreshLayout;
    //
    // private TestSortTask mTestSortTask;

    @Override
    public void onInitViews(@Nullable Bundle savedInstanceState) {
        // mRecyclerView = (TableView) findViewById(R.id.vh_recycler_view);
        // mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
    }

    @Override
    public void onInitDatas(@Nullable Bundle savedInstanceState) {
        // mRecyclerView.setDefaultSortType(TableView.SortType.Reverse);
        onRefresh();
    }

    @Override
    public void onInitListeners(@Nullable Bundle savedInstanceState) {
        // mRefreshLayout.setOnRefreshListener(this);
        // mRecyclerView.setOnRowClickListener(new TableView.OnRowClickListener() {
        //     @Override
        //     public void onRowClick(TableView view, TableData.Row row) {
        //         Toast.makeText(getApplicationContext(), String.format("第%1$s行（%2$s，原始在第%3$s行）",
        //                 row.getCurrentRowIndex(), row.getRowName(), row.getRawRowIndex()), Toast.LENGTH_SHORT).show();
        //     }
        // });
        // mRecyclerView.setOnSortListener(new TableView.OnSortListener() {
        //     @Override
        //     public void onSort(TableView view, int columnIndex, TableView.SortType sortType, List<TableData.Row> sortedRows) {
        //         Toast.makeText(getApplicationContext(), String.format("第%1$s列，当前按照%2$s排列",
        //                 columnIndex, sortType == TableView.SortType.Reverse ? "从大到小" : "从小到大"), Toast.LENGTH_SHORT).show();
        //     }
        // });
        // mRecyclerView.setOnValueClickListener(new TableView.OnValueClickListener() {
        //     @Override
        //     public void onValueClick(TableView view, TableData.Value value) {
        //         Toast.makeText(getApplicationContext(), String.format("第%1$s行第%2$s列，值是%3$s",
        //                 value.getCurrentRowIndex(), value.getColumnIndex(), value.getLabel()), Toast.LENGTH_SHORT).show();
        //     }
        // });
    }

    @Nullable
    @Override
    public View getLayout(@Nullable Bundle savedInstanceState, @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return inflater.inflate(R.layout.activity_main, parent, false);
    }

    @Override
    protected void onDestroy() {
        // if (mTestSortTask != null) {
        //     mTestSortTask.cancel(true);
        //     mTestSortTask = null;
        // }
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        // if (mTestSortTask != null) {
        //     mTestSortTask.cancel(true);
        //     mTestSortTask = null;
        // }
        // (mTestSortTask = new TestSortTask()).execute();
    }

    // private class TestSortTask extends AsyncTask<Void, Void, TableData> {
    //
    //     @Override
    //     protected TableData doInBackground(Void... voids) {
    //         TableData table = new TableData();
    //         table.setHasAvatar(true);
    //         for (int column = 0; column < COLUMNS_COUNT; column++) {
    //             table.addColumnName(String.format("Column%1$s", column));
    //         }
    //         for (int row = 0; row < ROW_COUNT; row++) {
    //             TableData.Row item = new TableData.Row("Row" + row);
    //             for (int column = 0; column < COLUMNS_COUNT; column++) {
    //                 item.addValue(new TableData.Value(Math.random()));
    //             }
    //             table.addRow(item);
    //         }
    //         return table;
    //     }
    //
    //     @Override
    //     protected void onPostExecute(TableData table) {
    //         mRecyclerView.setTable(table);
    //         mRefreshLayout.setRefreshing(false);
    //     }
    // }
}
