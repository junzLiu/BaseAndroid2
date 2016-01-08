package com.baseandroid.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.baseandroid.R;
import com.baseandroid.assist.widget.CustomAnimationProgressDialog;
import com.baseandroid.assist.widget.LoadingView;
import com.baseandroid.assist.widget.TitleBarLayout;

/**
 * Created by dell on 2015/11/2.
 */
public abstract class BaseActivity extends FragmentActivity {
    private View mRootView;
    private LinearLayout mContentLayout;
	private FrameLayout mGuideLayout;
	private List<Integer> mGuideLayoutRidList = new ArrayList<Integer>();
	private int mGuidePostion = 0;
	private LoadingView mLoadingView;
	private CustomAnimationProgressDialog mProgressDialog;
	protected TitleBarLayout mTitleBarLayout;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        setContentView(mRootView);
        
        mTitleBarLayout = (TitleBarLayout) mRootView.findViewById(R.id.layout_title);
		mContentLayout = (LinearLayout)mRootView.findViewById(R.id.layout_content);
		mGuideLayout = (FrameLayout)mRootView.findViewById(R.id.layout_guide);
		
		
		mContentLayout.addView(LayoutInflater.from(this).inflate(getLayoutId(), null));
        init();
        
        showGuide();
		mGuideLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mGuideLayoutRidList.size()>0){
					showGuide();	
				}
			}
		});
    }

    /**
     * get LayoutId
     *
     * @return LayoutId
     */
    protected abstract int getLayoutId();
    
    /**
     * init titleBar
     */
    protected void initTitleBar(){
    	
    }

    /**
     * init Ui wedget and init Data
     */
    protected abstract void init();

	/**
	 * add guide layout <br>
	 * it be showed in {@link BaseActivity#onCreate(Bundle)}
	 */
	protected void addGuideLayout(int layoutId) {
		mGuideLayoutRidList.add(layoutId);
	}
    
    /** show guide view  */
    protected void showGuide() {
		if (mGuideLayoutRidList.size() > 0) {
			
			if(mGuidePostion>=mGuideLayoutRidList.size()){
				mGuideLayout.removeAllViews();
				mGuideLayout.setVisibility(View.GONE);
				return;
			}
			
			if (mGuideLayout.getChildAt(0) != null)
				mGuideLayout.removeAllViews();

			mGuideLayout.addView(
					LayoutInflater.from(this).inflate(
							mGuideLayoutRidList.get(mGuidePostion), null),
					new LayoutParams(LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT));
			mGuideLayout.setVisibility(View.VISIBLE);
			mGuidePostion++;
			
				
		} else {
			mGuideLayout.setVisibility(View.GONE);
		}
	}
    
}
