package com.baseandroid.assist.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.baseandroid.R;


public class LoadingAnimImageView extends ImageView {

	private AnimationDrawable mAnimationDrawable;

	public LoadingAnimImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData();
	}

	public LoadingAnimImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initData();
	}

	public LoadingAnimImageView(Context context) {
		super(context);
		initData();
	}

	private void initData() {
		 this.setImageResource(R.drawable.loading_anim); 
		 mAnimationDrawable = (AnimationDrawable) this.getDrawable();  
	}

	private void startAnim() {
		mAnimationDrawable.start();
	}

	public void stopAnim() {
		mAnimationDrawable.stop();
	}

	@Override
	public void setVisibility(int visibility) { 
		super.setVisibility(visibility);
		if (visibility == View.VISIBLE) {
			startAnim();
		} else {
			stopAnim();
		}
	}
	
}
