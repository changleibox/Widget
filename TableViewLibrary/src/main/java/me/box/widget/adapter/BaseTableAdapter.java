package me.box.widget.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

/**
 * Created by box on 2017/6/29.
 * <p>
 * tableView的适配器
 */

public abstract class BaseTableAdapter implements TableAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    public BaseTableAdapter(@NonNull Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return mContext;
    }

}
