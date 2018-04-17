package cn.cntv.app.ipanda.ui.livechina.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不拦截事件
 * @author wuguicheng
 *
 */
public class NoInteruViewPager extends ViewPager {
	private boolean isScrollable;
	public NoInteruViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoInteruViewPager(Context context) {
		super(context);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onTouchEvent(ev);
		}

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isScrollable == false) {
			return false;
		} else {
			return super.onInterceptTouchEvent(ev);
		}

	}

	public boolean isScrollable() {
		return isScrollable;
	}

	public void setScrollable(boolean isScrollable) {
		this.isScrollable = isScrollable;
	}

}
