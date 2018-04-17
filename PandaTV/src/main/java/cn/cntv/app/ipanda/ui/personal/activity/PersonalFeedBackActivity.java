package cn.cntv.app.ipanda.ui.personal.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.personal.fragment.CommonQuestionFragment;
import cn.cntv.app.ipanda.ui.personal.fragment.MyQuestionFragment;

public class PersonalFeedBackActivity extends BaseActivity implements
		View.OnClickListener, OnPageChangeListener {

	private TextView titleLeft, titleCenter;
	private ViewPager mViewPager;
	private List<Fragment> mListFragment;
	private RadioButton mRb_left, mRb_right;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_feedback);

		initView();
	}

	private void initView() {
		titleLeft = (TextView) this.findViewById(R.id.common_title_left);
		titleCenter = (TextView) this.findViewById(R.id.common_title_center);
		mViewPager = (ViewPager) findViewById(R.id.feedback_content_viewpager);
		mRb_left = (RadioButton) findViewById(R.id.feedback_rb_left);
		mRb_right = (RadioButton) findViewById(R.id.feedback_rb_right);
		mRb_left.setOnClickListener(this);
		mRb_right.setOnClickListener(this);

		titleCenter.setText(getString(R.string.set_back_title));
		titleLeft.setOnClickListener(this);
		mRb_right.getPaint().setFakeBoldText(true);
		mRb_left.getPaint().setFakeBoldText(true);

		mListFragment = new ArrayList<Fragment>();
		mListFragment.add(new MyQuestionFragment());
		mListFragment.add(new CommonQuestionFragment());
		mViewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setOffscreenPageLimit(2);
		mRb_left.setChecked(true);
		refreshRadioButton();
	}

	class MyFragmentAdapter extends FragmentStatePagerAdapter {

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return mListFragment.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mListFragment.size();
		}

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.common_title_left:
			finish();
			break;
		case R.id.feedback_rb_left:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.feedback_rb_right:
			mViewPager.setCurrentItem(1);
			break;
		}
//		refreshRadioButton();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		switch(arg0){
		case 0:
			mRb_left.setChecked(true);
			break;
		case 1:
			mRb_right.setChecked(true);
			break;
		}
		refreshRadioButton();
	}
	
	public void refreshRadioButton()
	{
		Drawable bottom = getResources().getDrawable(R.drawable.custom_tab_indicator_selected2);
		bottom.setBounds(0, 0, getResources().getDisplayMetrics().widthPixels/2, bottom.getMinimumHeight());//必须设置图片大小，否则不显示
		if(mRb_left.isChecked())
		{
			mRb_left.setCompoundDrawables(null, null, null, bottom);
			mRb_left.setTextColor(Color.parseColor("#1E539C"));

			mRb_right.setCompoundDrawables(null, null, null, null);
			mRb_right.setTextColor(Color.BLACK);
		}
		else
		{
			mRb_right.setCompoundDrawables(null, null, null, bottom);
			mRb_right.setTextColor(Color.parseColor("#1E539C"));

			mRb_left.setCompoundDrawables(null, null, null, null);			
			mRb_left.setTextColor(Color.BLACK);
		}
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		// TODO Auto-generated method stub
//		if(ev.getAction() == MotionEvent.ACTION_DOWN){
//			if(getCurrentFocus()!=null && getCurrentFocus().getWindowToken()!=null){
//				InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);        		
//				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			}
//		}
//		return super.dispatchTouchEvent(ev);
//	}
	
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
 
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                top = l[1],
                bottom = top + v.getHeight(),
                right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }
 
    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
