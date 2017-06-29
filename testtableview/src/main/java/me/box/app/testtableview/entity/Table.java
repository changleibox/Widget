/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.app.testtableview.entity;

import android.util.SparseIntArray;
import android.view.ViewGroup.LayoutParams;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by box on 2017/6/15.
 * <p>
 * 每日必看数据
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class Table {

    private static final DecimalFormat DEFAULT_FORMAT = new DecimalFormat("###,##0");

    private final List<String> columnNames = new ArrayList<>();
    private final List<Row> rows = new ArrayList<>();
    private final SparseIntArray columnWidthArray = new SparseIntArray();
    private boolean hasAvatar;

    public Table() {
    }

    public Table(Table table) {
        if (table == null) {
            columnNames.clear();
            rows.clear();
            return;
        }
        this.setColumnNames(table.columnNames);
        this.setRows(table.rows);
    }

    public Table(List<String> columnNames) {
        this(columnNames, null);
    }

    public Table(List<String> columnNames, List<? extends Row> rows) {
        setColumnNames(columnNames);
        setRows(rows);
    }

    public <T extends Row> void addRow(T row) {
        getRows().add(row);
    }

    public void addColumnName(String columnName) {
        getColumnNames().add(columnName);
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames.clear();
        if (columnNames != null) {
            this.columnNames.addAll(columnNames);
        }
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<? extends Row> rows) {
        this.rows.clear();
        if (rows != null) {
            this.rows.addAll(rows);
        }
    }

    public SparseIntArray getColumnWidthArray() {
        return columnWidthArray;
    }

    public void setColumnWidthArray(SparseIntArray columnWidthArray) {
        this.columnWidthArray.clear();
        for (int i = 0; i < columnWidthArray.size(); i++) {
            int key = columnWidthArray.keyAt(i);
            this.columnWidthArray.put(key, columnWidthArray.get(key));
        }
    }

    public int getColumnWidth(int position) {
        return columnWidthArray.get(position);
    }

    public void setColumnWidth(int position, int width) {
        columnWidthArray.put(position, width);
    }

    public boolean isHasAvatar() {
        return hasAvatar;
    }

    public void setHasAvatar(boolean hasAvatar) {
        this.hasAvatar = hasAvatar;
    }

    public static class Row {

        private int rawRowIndex;
        private int currentRowIndex;
        private int height;
        private String rowName;
        private String photo;
        private final List<Value> values = new ArrayList<>();

        public Row(String rowName) {
            this(rowName, null);
        }

        public Row(String rowName, String photo) {
            this.rowName = rowName;
            this.photo = photo;
        }

        public List<Value> getValues() {
            return values;
        }

        public void setValues(List<? extends Value> values) {
            this.values.clear();
            if (values != null) {
                this.values.addAll(values);
            }
        }

        public <T extends Value> void addValue(T value) {
            getValues().add(value);
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getRowName() {
            return rowName;
        }

        public void setRowName(String rowName) {
            this.rowName = rowName;
        }

        public int getHeight() {
            return height == 0 ? LayoutParams.WRAP_CONTENT : height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getRawRowIndex() {
            return rawRowIndex;
        }

        public void setRawRowIndex(int rawRowIndex) {
            this.rawRowIndex = rawRowIndex;
        }

        public int getCurrentRowIndex() {
            return currentRowIndex;
        }

        public void setCurrentRowIndex(int currentRowIndex) {
            this.currentRowIndex = currentRowIndex;
        }
    }

    public static class Value {

        private double value;
        private String label;
        private int columnIndex;
        private int rawRowIndex;
        private int currentRowIndex;

        public Value(double value) {
            this(DEFAULT_FORMAT.format(value), value);
        }

        public Value(String label, double value) {
            this.value = value;
            this.label = label;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public int getColumnIndex() {
            return columnIndex;
        }

        public void setColumnIndex(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        public int getRawRowIndex() {
            return rawRowIndex;
        }

        public void setRawRowIndex(int rawRowIndex) {
            this.rawRowIndex = rawRowIndex;
        }

        public int getCurrentRowIndex() {
            return currentRowIndex;
        }

        public void setCurrentRowIndex(int currentRowIndex) {
            this.currentRowIndex = currentRowIndex;
        }
    }
}
