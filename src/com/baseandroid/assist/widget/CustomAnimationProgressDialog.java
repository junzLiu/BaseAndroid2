package com.baseandroid.assist.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.baseandroid.R;

/**
 * 带森巴动画的dialog
 * @author liumingjie
 * @date 2015.10.29
 *
 */
public class CustomAnimationProgressDialog extends Dialog {

	private LoadingAnimImageView mLoadingAnimImageView;
	private TextView mLoadingTextView;
	
	
	public CustomAnimationProgressDialog(Context context) {
		super(context, R.style.custom_dialog_theme);
		initView();
	}

	private void initView(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCanceledOnTouchOutside(false);
		setContentView(R.layout.dialog_custom_animation);
		mLoadingAnimImageView = (LoadingAnimImageView) findViewById(R.id.view_loadinganim);
		mLoadingTextView = (TextView) findViewById(R.id.tv_loading);
		mLoadingTextView.setVisibility(View.VISIBLE);
	}

	public CustomAnimationProgressDialog setLoadingText(String text){
		mLoadingTextView.setText(text);
		return this;
	}
	
	public CustomAnimationProgressDialog setLoadingText(int resourceId){
		mLoadingTextView.setText(resourceId);
		return this;
	}
	
	public CustomAnimationProgressDialog showDialog(){
		this.show();
		mLoadingAnimImageView.setVisibility(View.VISIBLE);
		return this;
	}
	
	public CustomAnimationProgressDialog dismissDialog(){
		mLoadingAnimImageView.setVisibility(View.GONE);
		this.dismiss();
		return this;
	}
	
	public CustomAnimationProgressDialog hideLoadingTextView(){
		mLoadingTextView.setVisibility(View.GONE);
		return this;
	}
}
