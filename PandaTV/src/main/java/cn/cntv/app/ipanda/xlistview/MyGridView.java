package cn.cntv.app.ipanda.xlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

public class MyGridView extends GridView {
	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGridView(Context context) {
		super(context);
	}

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


//	GestureDetector gest=new GestureDetector(new GestureDetector
//			.SimpleOnGestureListener(){
//		public boolean onScroll(MotionEvent e1, MotionEvent e2,
//				float distanceX, float distanceY) {
//			getParent().requestDisallowInterceptTouchEvent(false);
//			if(Math.abs(distanceX)>Math.abs(distanceY)){
//				return false;
//			}
//					return true;
//			
//		};
//	});
//	@Override
//	public boolean onInterceptHoverEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		getParent().requestDisallowInterceptTouchEvent(true);
//		return gest.onTouchEvent(event);
//	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		1073742229,536870911|-1610612737｜｜1073741825
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	
}