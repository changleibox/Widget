package me.box.app.testtableview.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import me.box.app.testtableview.impl.IContext

/**
 * Created by box on 2017/5/23.
 *
 * activity基类
 */

abstract class BaseActivity : AppCompatActivity(), IContext {

    private var mSavedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSavedInstanceState = savedInstanceState

        val inflater = LayoutInflater.from(this)
        val parent = findViewById(android.R.id.content) as ViewGroup
        val layout = getLayout(savedInstanceState, inflater, parent)
        if (layout != null) {
            setContentView(layout)
        }
    }

    override fun onContentChanged() {
        onInitViews(mSavedInstanceState)
        onInitDatas(mSavedInstanceState)
        onInitListeners(mSavedInstanceState)
    }

}
