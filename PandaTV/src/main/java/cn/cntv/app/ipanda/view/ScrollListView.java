package cn.cntv.app.ipanda.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author： pj
 * @Date： 2016年1月4日 下午2:40:38
 * @Description:解决ScrollView和ListView一起ListView显示不全的问题
 */
public class ScrollListView extends cn.cntv.app.ipanda.xlistview.XListView {

	public ScrollListView(Context context) {
		super(context);
	}

	public ScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	 public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	 // 根据模式计算每个child的高度和宽度
	 int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
	 MeasureSpec.AT_MOST);
	 super.onMeasure(widthMeasureSpec, expandSpec);
	 }

	private int maxHeight;

	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		if (maxHeight > -1) {
//			heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight,
//					MeasureSpec.AT_MOST);
//		}
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		System.out.println(getChildAt(0));
//	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setParentScrollAble(false);// 当手指触到listview的时候，让父ScrollView交出ontouch权限，也就是让父scrollview
										// 停住不能滚动
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			setParentScrollAble(true);// 当手指松开时，让父ScrollView重新拿到onTouch权限
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * 　　* 是否把滚动事件交给父scrollview 　　* 　　* @param flag 　　
	 */
	private void setParentScrollAble(boolean flag) {
		this.getParent().getParent().getParent()
				.requestDisallowInterceptTouchEvent(!flag);// 这里的parentScrollView就是listview外面的那个scrollview
	}
	// @Override
	// public boolean onTouchEvent(MotionEvent ev) {
	// // 当手指按下的时候，获取到点击那个球
	// int x = (int) ev.getX();
	// int y = (int) ev.getY();
	// // The position of the item which contains the specified point, or
	// // INVALID_POSITION if the point does not intersect an item.
	// int position = pointToPosition(x, y);
	//
	// if (position == INVALID_POSITION) {
	// return false;
	// }
	//
	// switch (ev.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// // 当手指按下的时候，接管ScrollView滑动
	// // this.getParent();//获取到LinearLayout
	// this.getParent().getParent().getParent()
	// .requestDisallowInterceptTouchEvent(true);
	// break;
	// case MotionEvent.ACTION_MOVE:
	// break;
	// case MotionEvent.ACTION_UP:
	// // 当手指按下的时候，放行，ScrollView滑动
	// this.getParent().getParent().getParent()
	// .requestDisallowInterceptTouchEvent(false);
	// // 增加一个监听
	// break;
	// }
	// return super.onTouchEvent(ev);
	// }

}
