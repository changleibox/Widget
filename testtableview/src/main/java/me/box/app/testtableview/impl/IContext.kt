package me.box.app.testtableview.impl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by box on 2017/5/23.
 *
 * 自己实现的上下文环境
 */
interface IContext {

    fun onInitViews(savedInstanceState: Bundle?)

    fun onInitDatas(savedInstanceState: Bundle?)

    fun onInitListeners(savedInstanceState: Bundle?)

    fun getLayout(savedInstanceState: Bundle?, inflater: LayoutInflater, parent: ViewGroup): View?
}