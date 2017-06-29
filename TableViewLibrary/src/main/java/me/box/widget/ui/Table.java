/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.widget.ui;

import android.util.SparseIntArray;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by box on 2017/6/15.
 * <p>
 * 每日必看数据
 */

public class Table {

    private int columnCount;
    private final List<Row> rows = new ArrayList<>();
    private final SparseIntArray columnWidthArray = new SparseIntArray();

    Table() {
    }

    Table(Table table) {
        if (table == null) {
            rows.clear();
            return;
        }
        this.columnCount = table.getColumnCount();
        this.setRows(table.rows);
    }

    Table(List<? extends Row> rows) {
        setRows(rows);
    }

    <T extends Row> void addRow(T row) {
        getRows().add(row);
    }

    public List<Row> getRows() {
        return rows;
    }

    void setRows(List<? extends Row> rows) {
        this.rows.clear();
        if (rows != null) {
            this.rows.addAll(rows);
        }
    }

    public SparseIntArray getColumnWidthArray() {
        return columnWidthArray;
    }

    void setColumnWidthArray(SparseIntArray columnWidthArray) {
        this.columnWidthArray.clear();
        for (int i = 0; i < columnWidthArray.size(); i++) {
            int key = columnWidthArray.keyAt(i);
            this.columnWidthArray.put(key, columnWidthArray.get(key));
        }
    }

    public int getColumnWidth(int position) {
        return columnWidthArray.get(position);
    }

    void setColumnWidth(int position, int width) {
        columnWidthArray.put(position, width);
    }

    public int getColumnCount() {
        return columnCount;
    }

    void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public static class Row {

        private int rawRowIndex;
        private int currentRowIndex;
        private int height;
        private final List<Value> values = new ArrayList<>();

        Row() {
        }

        public List<Value> getValues() {
            return values;
        }

        void setValues(List<? extends Value> values) {
            this.values.clear();
            if (values != null) {
                this.values.addAll(values);
            }
        }

        <T extends Value> void addValue(T value) {
            getValues().add(value);
        }

        public int getHeight() {
            return height == 0 ? LayoutParams.WRAP_CONTENT : height;
        }

        void setHeight(int height) {
            this.height = height;
        }

        public int getRawRowIndex() {
            return rawRowIndex;
        }

        void setRawRowIndex(int rawRowIndex) {
            this.rawRowIndex = rawRowIndex;
        }

        public int getCurrentRowIndex() {
            return currentRowIndex;
        }

        void setCurrentRowIndex(int currentRowIndex) {
            this.currentRowIndex = currentRowIndex;
        }
    }

    public static class Value {

        private int columnIndex;
        private int rawRowIndex;
        private int currentRowIndex;

        Value() {
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        void setColumnIndex(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        public int getRawRowIndex() {
            return rawRowIndex;
        }

        void setRawRowIndex(int rawRowIndex) {
            this.rawRowIndex = rawRowIndex;
        }

        public int getCurrentRowIndex() {
            return currentRowIndex;
        }

        void setCurrentRowIndex(int currentRowIndex) {
            this.currentRowIndex = currentRowIndex;
        }
    }
}
