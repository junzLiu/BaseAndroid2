package com.baseandroid.assist.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baseandroid.R;


/**
 * @module
 * @title
 * @function
 * @author Mark
 * @version 1.0
 * @createTime 2015年10月22日 下午5:29:55
 */

public class ListPopWindow implements View.OnClickListener {
	private Context mContext;

	private PopupWindow mPopWindow;
	private View mPopWindowView;
	private View mOutSideView;
	private View mListFrontLine;
	private TextView mTitleView;
	private TextView mContentView;
	private ListView mListView;
	private Button mCancelBtn;
	private LinearLayout mContainerLl;

	private PopListAdapter mListAdapter;

	private AlphaAnimation animFadeIn;

	private AlphaAnimation animFadeOut;

	private TranslateAnimation animShow;

	private TranslateAnimation animHide;

	private boolean isOutSideCancelEnable = true;

	public ListPopWindow(Context context) {
		this.mContext = context;
		init();
	}

	private void init() {
		mPopWindowView = LayoutInflater.from(mContext).inflate(R.layout.view_list_pop, null);
		mOutSideView = mPopWindowView.findViewById(R.id.pop_v_outside);
		mTitleView = (TextView) mPopWindowView.findViewById(R.id.pop_tv_title);
		mContentView = (TextView) mPopWindowView.findViewById(R.id.pop_tv_content);
		mListView = (ListView) mPopWindowView.findViewById(R.id.pop_lv);
		mCancelBtn = (Button) mPopWindowView.findViewById(R.id.pop_btn_cancel);
		mContainerLl = (LinearLayout) mPopWindowView.findViewById(R.id.pop_ll_container);
		mListFrontLine = mPopWindowView.findViewById(R.id.pop_list_front_line);
		mTitleView.setVisibility(View.GONE);
		mContentView.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
		mCancelBtn.setVisibility(View.GONE);
		mListFrontLine.setVisibility(View.GONE);
		mCancelBtn.setOnClickListener(this);
		mOutSideView.setOnClickListener(this);
		mPopWindow = new PopupWindow(mPopWindowView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		initAnim();
	}

	/**
	 * 点击弹框外是否关闭弹框
	 * 
	 * @param isEnable
	 * @return
	 */
	public ListPopWindow setOutSideCancelEnable(boolean isEnable) {
		isOutSideCancelEnable = isEnable;
		return this;
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 * @return
	 */
	public ListPopWindow setTitle(String title) {
		mTitleView.setText(title);
		mTitleView.setVisibility(View.VISIBLE);
		return this;
	}

	/**
	 * 设置标题
	 * 
	 * @param titleId
	 * @return
	 */
	public ListPopWindow setTitle(int titleId) {
		mTitleView.setText(titleId);
		mTitleView.setVisibility(View.VISIBLE);
		return this;
	}

	/**
	 * 设置文本内容
	 * 
	 * @param content
	 * @return
	 */
	public ListPopWindow setContent(String content) {
		mContentView.setText(content);
		mContentView.setVisibility(View.VISIBLE);
		return this;
	}

	/**
	 * 设置文本内容
	 * 
	 * @param contentId
	 * @return
	 */
	public ListPopWindow setContent(int contentId) {
		mContentView.setText(contentId);
		mContentView.setVisibility(View.VISIBLE);
		return this;
	}

	/**
	 * 设置列表数据
	 * 
	 * @param strIds
	 *            数据
	 * @param listener
	 *            OnItemClickListener接口
	 * @return
	 */
	public ListPopWindow setListData(int[] strIds, OnItemClickListener listener) {
		String[] strs = new String[strIds.length];

		for (int i = 0; i < strIds.length; i++) {
			strs[i] = mContext.getString(strIds[i]);
		}

		return setListData(strs, listener);
	}

	/**
	 * 设置列表数据
	 * 
	 * @param strs
	 *            数据
	 * @param listener
	 *            OnItemClickListener接口
	 * @return
	 */
	public ListPopWindow setListData(String[] strs, OnItemClickListener listener) {
		mListAdapter = new PopListAdapter(mContext, strs);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(listener);
		mListView.setVisibility(View.VISIBLE);
		if(mContentView.getVisibility() == View.VISIBLE || mTitleView.getVisibility() == View.VISIBLE)
			mListFrontLine.setVisibility(View.VISIBLE);
		return this;
	}

	/**
	 * 是否需要取消按钮
	 * 
	 * @param isNeedCancelBtn
	 * @return
	 */
	public ListPopWindow setNeedCancelBtn(boolean isNeedCancelBtn) {
		if (isNeedCancelBtn)
			mCancelBtn.setVisibility(View.VISIBLE);
		else
			mCancelBtn.setVisibility(View.GONE);
		return this;
	}

	public void show(View parentView) {
		if (mPopWindow == null)
			init();
		mPopWindowView.setFocusable(true);
		mPopWindowView.setFocusableInTouchMode(true);
		mPopWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
		mContainerLl.clearAnimation();
		mPopWindowView.clearAnimation();
		mPopWindowView.startAnimation(animFadeIn);
		mContainerLl.startAnimation(animShow);
	}

	public void dismiss() {
		if (mPopWindow != null && mPopWindow.isShowing()) {
			mPopWindowView.clearAnimation();
			mPopWindowView.startAnimation(animFadeOut);
			mContainerLl.clearAnimation();
			mContainerLl.startAnimation(animHide);
		}
	}

	public boolean isShowing() {
		return mPopWindow != null && mPopWindow.isShowing();
	}

	private void initAnim() {
		animFadeIn = new AlphaAnimation(0, 1);
		animFadeIn.setDuration(200);
		animFadeOut = new AlphaAnimation(1, 0);
		animFadeOut.setDuration(200);

		animShow = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
		animShow.setDuration(300);

		animHide = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
		animHide.setDuration(300);
		animHide.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				mPopWindow.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_btn_cancel:
			if (mPopWindow != null && mPopWindow.isShowing())
				dismiss();
			break;
		case R.id.pop_v_outside:
			if (mPopWindow != null && mPopWindow.isShowing() && isOutSideCancelEnable)
				dismiss();
			break;
		}
	}

	class PopListAdapter extends BaseAdapter {
		private String[] mStrs;
		private Context mContext;

		public PopListAdapter(Context context, String[] strs) {
			this.mStrs = strs;
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return mStrs.length;
		}

		@Override
		public String getItem(int position) {
			return mStrs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(mContext);
			tv.setTextColor(mContext.getResources().getColor(R.color.popup_item_text_color));
			tv.setTextSize(16);
			int padding = (int)(12*mContext.getResources().getDisplayMetrics().density);
			tv.setPadding(padding, padding, padding, padding);
			tv.setGravity(Gravity.CENTER);
			tv.setText(getItem(position));
			return tv;
		}

	}

}
