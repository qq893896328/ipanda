package cn.cntv.app.ipanda.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;


/**
 * 熊猫观察模块使用
 * 
 * @author wanghaofei
 * 
 */
public class PEViewPager extends ViewPager {

	public PEViewPager(Context context) {
		super(context);
	}

	public PEViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v instanceof PEHCustomHScrollView) {
			return false;
		}
		return super.canScroll(v, checkV, dx, x, y);
	}
}
