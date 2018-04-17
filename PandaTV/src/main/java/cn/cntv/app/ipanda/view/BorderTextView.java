package cn.cntv.app.ipanda.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import cn.cntv.app.ipanda.ui.home.auto.layout.AutoLinearLayout;

public class BorderTextView extends AutoLinearLayout {

	private int mTextColor;

	public BorderTextView(Context context) {
		this(context, null);
	}

	public BorderTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private int mSrokeWidth = 5;

	@Override
	protected void dispatchDraw(Canvas canvas) {

		// 获取布局控件宽高
		int width = getWidth();
		int height = getHeight();

		// // 创建画笔
		Paint mPaint = new Paint();

		// // 设置画笔的各个属性
		mPaint.setColor(mTextColor);

		// 画TextView的4个边
		canvas.drawLine(0, 0, width - mSrokeWidth, 0, mPaint);
		canvas.drawLine(0, 0, 0, height - mSrokeWidth, mPaint);
		canvas.drawLine(width - mSrokeWidth, 0, width - mSrokeWidth, height
				- mSrokeWidth, mPaint);
		canvas.drawLine(0, height - mSrokeWidth, width - mSrokeWidth, height
				- mSrokeWidth, mPaint);
		// 最后必须调用父类的方法
		super.dispatchDraw(canvas);
	}

	public void setTextColor(int textColor) {

		mTextColor = textColor;
	}

}
