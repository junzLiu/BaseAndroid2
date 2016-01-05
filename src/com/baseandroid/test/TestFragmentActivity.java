package com.baseandroid.test;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.baseandroid.R;
import com.baseandroid.app.activity.BaseActivity;


/**
 * @author Mark
 * @version 1.0
 * @module
 * @title
 * @function
 * @createTime 2015年10月8日 上午10:04:46
 */

public class TestFragmentActivity extends BaseActivity {


    Fragment mFragment;
    FrameLayout mContent;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void init() {
        mFragment = (Fragment) getIntent().getSerializableExtra(TestUIActivity.EXTRA_FRAGMENT);
        mContent = (FrameLayout) findViewById(R.id.content);
        if (mFragment == null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().add(R.id.content, mFragment).commit();
        }
    }

}
