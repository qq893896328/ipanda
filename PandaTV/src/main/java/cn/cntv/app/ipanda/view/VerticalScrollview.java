package cn.cntv.app.ipanda.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class VerticalScrollview extends ScrollView {

	public VerticalScrollview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		float startx = ev.getX();
//		float starty = ev.getY();
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_MOVE:
//			float x = ev.getX();
//			float y = ev.getY();
//			if(Math.abs(y-starty) >Math.abs(x-startx) )
//			{
//				return true;
//			}
//			break;
//		}
//		return false;
//	}
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		// TODO Auto-generated method stub
//			return false;
//			
//			//onTouchEvent(ev)	;	
//	}
//	private Boolean haveScrollbars;
//	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
//	        if (haveScrollbars == false) {   
//	            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);   
//	            super.onMeasure(widthMeasureSpec, expandSpec);   
//	        } else {   
//	            super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
//	        }   
//	    }   
}