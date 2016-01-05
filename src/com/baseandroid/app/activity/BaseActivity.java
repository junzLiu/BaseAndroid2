package com.baseandroid.app.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by dell on 2015/11/2.
 */
public abstract class BaseActivity extends FragmentActivity {
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        init();
        setContentView(getLayoutId());
    }

    /**
     * get LayoutId
     *
     * @return LayoutId
     */
    protected abstract int getLayoutId();

    /**
     * init Ui wedget and init Data
     */
    protected abstract void init();

}
