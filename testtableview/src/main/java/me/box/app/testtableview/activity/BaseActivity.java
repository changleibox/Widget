/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.app.testtableview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.box.app.testtableview.impl.IContext;

/**
 * Created by box on 2017/5/23.
 *
 * activity基类
 */

public abstract class BaseActivity extends AppCompatActivity implements IContext {

    private Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;

        LayoutInflater inflater = LayoutInflater.from(this);
        ViewGroup parent = (ViewGroup) findViewById(android.R.id.content);
        View layout = getLayout(savedInstanceState, inflater, parent);
        if (layout != null) {
            setContentView(layout);
        }
    }

    @Override
    public void onContentChanged() {
        onInitViews(mSavedInstanceState);
        onInitDatas(mSavedInstanceState);
        onInitListeners(mSavedInstanceState);
    }

}
