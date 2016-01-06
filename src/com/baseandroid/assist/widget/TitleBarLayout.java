package com.baseandroid.assist.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baseandroid.R;
import com.baseandroid.assist.tools.LogUtil;
import com.baseandroid.assist.tools.TransformUtil;

/**
 * titleBar
 * 
 * @author liumingjie
 * @data 2015.05.04
 * 
 */
public class TitleBarLayout extends RelativeLayout implements OnItemClickListener {
	private static final String TAG = "TitleBarLayout";
	private static final int DEFAULT_BANK_ICON = R.drawable.ic_title_back;
	private static final int DEFAULT_NAVI_ICON = R.drawable.navi_more;
	private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

	private LinearLayout mLeftLayout;
	private LinearLayout mCenterLayout;
	private LinearLayout mRightLayout;

	private ImageView mCloseIv;
	private ImageView mBackIndicator;
	private ImageView mIconIv;
	private TextView mTitleTextTv;
	private ImageView mNaviIv;
	private PopupWindow mPopupWindow;
	private ImageView mTitleImageView;

	private ArrayList<NaviItem> mNaviList;
	private NaviListAdapter mAdapter;
	private TitleBackListener mBackListener;
	private TitleNaviItemsListener mNaviItemsListener;
	private ActionListener mActionListener;

	private List<ActionItem> mActions;

	public interface ActionListener {
		void onActionPerformed(int id);
	};

	public interface TitleBackListener {
		void onBackClick();
		void onCloseClick();
	}

	public interface TitleNaviItemsListener {
		void onNaviItemClick(int position);
	}

	public void setActionListener(ActionListener listener) {
		mActionListener = listener;
	}

	public void setTitleBackListener(TitleBackListener l) {
		mBackListener = l;
	}

	public void setTitleNaviItemsListener(TitleNaviItemsListener l) {
		mNaviItemsListener = l;
	}

	private static final Handler sHandler = new Handler() {
		public void handleMessage(Message msg) {
			LogUtil.d("TitleBarLayout", "msg:" + msg.what);
			TitleBarLayout obj = (TitleBarLayout) msg.obj;
			if (obj.mActionListener != null) {
				obj.mActionListener.onActionPerformed(msg.what);
			}
		};
	};

	public TitleBarLayout(Context context) {
		super(context);
		init();
	}

	public TitleBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setGravity(Gravity.CENTER_HORIZONTAL);
		addView(makeLeftLayout());
		addView(makeCenterLayout());
		addView(makeRightLayout());
		setBackgroundResource(R.drawable.home_top_bg);
		requestLayout();
		mCloseIv.setVisibility(View.GONE);
	}

	private View makeLeftLayout() {
		mLeftLayout = createLayout(RelativeLayout.ALIGN_PARENT_LEFT);
		mLeftLayout.addView(makeBackIv());
		mLeftLayout.addView(makeCloseIv());
		return mLeftLayout;
	}

	@SuppressWarnings("unused")
	private View makeEmptyView(int width) {
		View v = new View(getContext());
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lp.addRule(Gravity.LEFT);
		lp.width = width;
		v.setLayoutParams(lp);
		return v;

	}

	private View makeCenterLayout() {
		mCenterLayout = createLayout(RelativeLayout.CENTER_IN_PARENT);
		mCenterLayout.addView(makeTitleTextTv());
		mCenterLayout.addView(makeTitleImageView());
		return mCenterLayout;
	}

	private View makeRightLayout() {
		mRightLayout = createLayout(RelativeLayout.ALIGN_PARENT_RIGHT);
		mRightLayout.addView(makeNaviIv());

		return mRightLayout;
	}

	private ImageView makeBackIv() {
		mBackIndicator = new ImageView(getContext());
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lp.width = TransformUtil.dip2px(getContext(), 40);
		mBackIndicator.setLayoutParams(lp);
		mBackIndicator.setScaleType(ScaleType.CENTER);
		mBackIndicator.setOnClickListener(new ViewClickListener());
		mBackIndicator.setImageResource(DEFAULT_BANK_ICON);
		mBackIndicator.setBackgroundResource(R.drawable.action_item_bg);
		return mBackIndicator;
	}
	
	private ImageView makeCloseIv(){
		mCloseIv = new ImageView(getContext());
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lp.width = TransformUtil.dip2px(getContext(), 40);
		mCloseIv.setLayoutParams(lp);
		mCloseIv.setScaleType(ScaleType.CENTER);
		mCloseIv.setOnClickListener(new ViewClickListener());
		mCloseIv.setImageResource(R.drawable.icon_title_close);
		mCloseIv.setBackgroundResource(R.drawable.action_item_bg);
		return mCloseIv;
	}

	private TextView makeTitleTextTv() {
		mTitleTextTv = new ScrollingTextView(getContext());
		mTitleTextTv.setTextSize(20);
		mTitleTextTv.setTextColor(DEFAULT_TEXT_COLOR);
		return mTitleTextTv;
	}

	private ImageView makeTitleImageView() {
		mTitleImageView = new ImageView(getContext());
		return mTitleImageView;
	}

	private LinearLayout createLayout(int gravity) {
		LinearLayout ll = new LinearLayout(getContext());
		LayoutParams rl = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		rl.addRule(gravity);
		ll.setLayoutParams(rl);
		ll.setGravity(Gravity.CENTER_VERTICAL);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		return ll;
	}

	private static class ActionItem {
		public ActionItem(String title, int icon, int id, boolean clickable) {
			this.title = title;
			this.icon = icon;
			this.id = id;
			this.clickable = clickable;
		}

		String title;
		int icon;
		int id;
		boolean clickable;
	}

	@SuppressLint("NewApi")
	private View createActionView(final ActionItem ai) {
		View view = null;
		if (!TextUtils.isEmpty(ai.title)) {
			if (ai.clickable) {
				TextView btn = new TextView(getContext());
				btn.setText(ai.title);
				btn.setTextSize(15);
				btn.setClickable(true);
				ColorStateList csl = (ColorStateList) this.getResources().getColorStateList(
						R.color.dairy_top_text_selector);
				if (csl != null) {
					btn.setTextColor(csl);
				} else {
					btn.setTextColor(getContext().getResources().getColor(R.color.dairy_top_text_selector));
				}
				btn.setGravity(Gravity.CENTER);
				if (ai.icon != 0) {
					btn.setBackgroundResource(ai.icon);
				} else {
					btn.setBackgroundResource(0);
				}
				view = btn;
			} else {
				TextView tv = new TextView(getContext());
				tv.setText(ai.title);
				tv.setTextSize(13);
				ColorStateList csl = (ColorStateList) this.getResources().getColorStateList(
						R.color.dairy_top_text_selector);
				if (csl != null) {
					tv.setTextColor(csl);
				} else {
					tv.setTextColor(getContext().getResources().getColor(R.color.dairy_top_text_selector));
				}
				tv.setGravity(Gravity.CENTER);
				view = tv;
			}
		} else {
			ImageView iv = new ImageView(getContext());
			iv.setImageResource(ai.icon);
			view = iv;
			if (!ai.clickable) {
				iv.setBackgroundResource(R.drawable.action_item_bg);
			}
		}
		view.setPadding(TransformUtil.dip2px(getContext(), 18), 0, TransformUtil.dip2px(getContext(), 18), 0);

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.MATCH_PARENT);
		view.setLayoutParams(lp);

		view.setClickable(true);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = sHandler.obtainMessage(ai.id, TitleBarLayout.this);
				msg.sendToTarget();
			}
		});
		return view;
	}

	private void invalidActions() {
		mRightLayout.removeAllViews();
		if (mActions == null) {
			return;
		}
		for (ActionItem ai : mActions) {
			mRightLayout.addView(createActionView(ai));
		}
	}

	public void setActionLayoutState(int viewState) {
		mRightLayout.setVisibility(viewState);
	}

	public void addAction(String title, int icon, int id, boolean clickable) {
		if (mActions == null) {
			mActions = new ArrayList<ActionItem>();
		}
		mActions.add(new ActionItem(title, icon, id, clickable));
		invalidActions();
	}

	public boolean hasAction(int id) {
		boolean has = false;
		if (mActions == null || mActions.size() == 0) {
			return has;
		}
		for (ActionItem actionItem : mActions) {
			if (actionItem.id == id) {
				has = true;
				break;
			} else {
				has = false;
			}
		}
		return has;
	}

	/**
	 * 重新设置 action的文字
	 * 
	 * @param id
	 * @param title
	 */
	public void setTextAction(int id, String title, boolean clickable) {
		int index = -1;
		for (int i = 0; i < mActions.size(); i++) {
			ActionItem ai = mActions.get(i);
			if (ai.id == id) {
				ai.clickable = clickable;
				index = i;
				break;
			}
		}
		if (index == -1) {
			LogUtil.d(TAG, "id not found");
			return;
		}
		try {
			TextView tv = (TextView) mRightLayout.getChildAt(index);
			tv.setText(title);
			if (clickable) {
				ColorStateList csl = (ColorStateList) this.getResources().getColorStateList(
						R.color.dairy_top_text_selector);
				if (csl != null) {
					tv.setTextColor(csl);
				} else {
					tv.setTextColor(getContext().getResources().getColor(R.color.dairy_top_text_selector));
				}
			} else {
				tv.setTextColor(getContext().getResources().getColor(R.color.dairy_day_text));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重新设置action的icon
	 * 
	 * @param id
	 * @param icon
	 */
	public void setIconAction(int id, int icon) {
		int index = -1;
		for (int i = 0; i < mActions.size(); i++) {
			ActionItem ai = mActions.get(i);
			if (ai.id == id) {
				index = i;
				break;
			}
		}
		if (index == -1) {
			LogUtil.d(TAG, "id not found");
			return;
		}
		try {
			ImageView iv = (ImageView) mRightLayout.getChildAt(index);
			iv.setImageResource(icon);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加文字action
	 * 
	 * @param title
	 *            文字
	 * @param id
	 *            id
	 * @param clickable
	 *            是否可以点击
	 */
	public void addTextAction(String title, int id, boolean clickable) {
		if (TextUtils.isEmpty(title)) {
			throw new IllegalArgumentException("title cannot be empty!");
		}
		addAction(title, 0, id, clickable);
	}

	/**
	 * 添加图标action
	 * 
	 * @param icon
	 *            icon资源
	 * @param id
	 *            id
	 * @param clickable
	 *            是否可以点击
	 */
	public void addIconAction(int icon, int id, boolean clickable) {
		if (icon == 0) {
			throw new IllegalArgumentException("icon cannot be empty!");
		}
		addAction(null, icon, id, clickable);
	}

	public void removeActions() {
		mActions = null;
		invalidActions();
	}

	private ImageView makeNaviIv() {
		mNaviIv = new ImageView(getContext());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		lp.width = TransformUtil.dip2px(getContext(), 60);
		mNaviIv.setLayoutParams(lp);
		mNaviIv.setScaleType(ScaleType.CENTER);
		mNaviIv.setImageResource(DEFAULT_NAVI_ICON);
		mNaviIv.setBackgroundResource(R.drawable.action_item_bg);
		mNaviIv.setOnClickListener(new ViewClickListener());
		return mNaviIv;
	}

	private void showPopupWindow() {
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(getContext());
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setWidth(TransformUtil.dip2px(getContext(), 150));
			mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.nav_pop_bg));
		}
		mPopupWindow.setContentView(createListView());
		// TODO need test
		mPopupWindow.showAtLocation(this, Gravity.RIGHT | Gravity.TOP, TransformUtil.dip2px(getContext(), 10),
				TransformUtil.dip2px(getContext(), 74));
	}

	private ListView createListView() {
		ListView listView = new DropDownListView(getContext(), false);
		mAdapter = new NaviListAdapter(getContext(), mNaviList);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);
		listView.setFocusable(true);
		listView.setFocusableInTouchMode(true);
		listView.setDivider(getResources().getDrawable(R.color.white));
		listView.setDividerHeight(1);
		listView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT,
				AbsListView.LayoutParams.WRAP_CONTENT));
		return listView;
	}

	public void notifySetChange() {
		if (mAdapter != null)
			mAdapter.notifyDataSetChanged();
	}

	public void setTitleIcon(int resId) {
		mIconIv.setImageResource(resId);
	}

	public void showTitleIcon(boolean show) {
		mIconIv.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	public void showBackIndicator(boolean isShow) {
		if (isShow) {
			mBackIndicator.setVisibility(View.VISIBLE);
			mLeftLayout.setOnClickListener(new ViewClickListener());
		} else {
			mBackIndicator.setVisibility(View.GONE);
			mLeftLayout.setOnClickListener(null);
			mLeftLayout.setClickable(false);
		}
	}

	public void showCloseIndicator(boolean isShow){
		if(isShow){
			mCloseIv.setVisibility(View.VISIBLE);
		}else{
			mCloseIv.setVisibility(View.GONE);
		}
	}
	
	public void setTitleText(String text) {
		mTitleTextTv.setText(text);
	}

	public void setTitleText(int resId) {
		mTitleTextTv.setText(resId);
	}

	public void setTitleImageView(int resid) {
		mTitleImageView.setBackgroundResource(resid);
	}

	public void showNavigation(boolean isShow) {
		if (isShow) {
			mNaviIv.setVisibility(View.VISIBLE);
		} else {
			mNaviIv.setVisibility(View.GONE);
		}
	}

	public void setNavigationList(ArrayList<NaviItem> itemList) {
		mNaviList = itemList;
	}
	
	public ArrayList<NaviItem> getNavigationList(){
		return mNaviList;
	}

	public void addNavigationItem(int iconId, String itemName) {
		if (mNaviList == null) {
			mNaviList = new ArrayList<NaviItem>();
		}
		mNaviList.add(new NaviItem(iconId, itemName));
	}

	private class ViewClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v == mBackIndicator) {
				if (mBackListener != null) {
					mBackListener.onBackClick();
				}
			} else if (v == mNaviIv) {
				if (mNaviList != null && mNaviList.size() > 0) {
					showPopupWindow();
				}
			} else if(v == mCloseIv){
				if(mBackListener != null){
					mBackListener.onCloseClick();
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mPopupWindow.dismiss();
		if (mNaviItemsListener != null) {
			mNaviItemsListener.onNaviItemClick(arg2);
		}
	}

	private static class NaviListAdapter extends BaseAdapter {
		private List<NaviItem> mNaviList;
		private LayoutInflater mInflater;

		public NaviListAdapter(Context context, List<NaviItem> list) {
			super();
			mInflater = LayoutInflater.from(context);
			mNaviList = list;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			if (convertView == null) {
				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.navigation_list_item, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.navigation_list_item_icon);
				holder.name = (TextView) convertView.findViewById(R.id.navigation_list_item_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			/*
			 * if(position == 0){ convertView.setPadding(40, 50, 40, 30); }else{
			 * convertView.setPadding(40, 40, 40, 40); }
			 */
			NaviItem item = mNaviList.get(position);
			holder.icon.setImageResource(item.getIconId());
			holder.name.setText(item.getName());
			return convertView;
		}

		private class ViewHolder {
			ImageView icon;
			TextView name;
		}

		@Override
		public int getCount() {
			return mNaviList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	class DropDownListView extends ListView {
		private boolean mListSelectionHidden;

		private boolean mHijackFocus;

		public DropDownListView(Context context, boolean hijackFocus) {
			super(context, null);
			mHijackFocus = hijackFocus;
			setCacheColorHint(0);
			// Transparent, since the background drawable
			// could be anything.
			setSelector(R.color.transparent);
		}

		@Override
		public boolean isInTouchMode() {
			// WARNING: Please read the comment where mListSelectionHidden is
			// declared
			return (mHijackFocus && mListSelectionHidden) || super.isInTouchMode();
		}

		@Override
		public boolean hasWindowFocus() {
			return mHijackFocus || super.hasWindowFocus();
		}

		@Override
		public boolean isFocused() {
			return mHijackFocus || super.isFocused();
		}

		@Override
		public boolean hasFocus() {
			return mHijackFocus || super.hasFocus();
		}
	}

	public static class NaviItem {

		private int iconId;
		private String name;

		public NaviItem() {

		}

		public NaviItem(int iconId, String name) {
			this.iconId = iconId;
			this.name = name;
		}

		public int getIconId() {
			return iconId;
		}

		public void setIconId(int iconId) {
			this.iconId = iconId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
