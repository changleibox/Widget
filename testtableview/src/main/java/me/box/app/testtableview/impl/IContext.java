/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package me.box.app.testtableview.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by box on 2017/5/23.
 *
 * 自己实现的上下文环境
 */
public interface IContext {

    void onInitViews(Bundle savedInstanceState);

    void onInitDatas(Bundle savedInstanceState);

    void onInitListeners(Bundle savedInstanceState);

    View getLayout(Bundle savedInstanceState, LayoutInflater inflater, ViewGroup parent);
}