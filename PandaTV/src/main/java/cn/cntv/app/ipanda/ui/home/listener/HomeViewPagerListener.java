package cn.cntv.app.ipanda.ui.home.listener;

import android.support.v4.view.ViewPager.OnPageChangeListener;

import cn.cntv.app.ipanda.view.PointView;

/**
 * @ClassName: HomeViewPagerListener
 * @author Xiao JinLai
 * @Date Dec 29, 2015 5:07:34 PM
 * @Description：
 */
public class HomeViewPagerListener implements OnPageChangeListener {

	private PointView mPointView;

	public HomeViewPagerListener(PointView pointView) {

		this.mPointView = pointView;
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

		mPointView.setCurrentIndex(position-1);
	}

}
