package cn.cntv.app.ipanda.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.utils.DensityUtil;

/**
 * @ClassName: PointView
 * @author Xiao JinLai
 * @Date Dec 24, 2015 6:02:16 PM
 * @Description：滚动图标签提示
 */
public class PointView extends View {

	private Context mContext;
	private Paint mPaint;

	private Bitmap mBitmapOn, mBitmapOff;

	private int mCurrentIndex;
	private int mAllCount;

	private int mOnOffWidthDif, mOnOffHeightDif;

	private int mViewHeight;

	private int mScreenWidth;
	private int mImgMargin;

	public PointView(Context context) {
		this(context, null);
	}

	public PointView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		this.mContext = context;
		mPaint = new Paint();

		initBitmap();
		WindowManager tManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		mScreenWidth = tManager.getDefaultDisplay().getWidth();
	}

	/**
	 * 加载图片
	 */
	private void initBitmap() {

		Drawable tDrawableOn = mContext.getResources().getDrawable(
				R.drawable.point_on);

		Drawable tDrawableOff = mContext.getResources().getDrawable(
				R.drawable.point_off);

		mBitmapOn = ((BitmapDrawable) tDrawableOn).getBitmap();
		mBitmapOff = ((BitmapDrawable) tDrawableOff).getBitmap();

		int tOnWidth = mBitmapOn.getWidth();
		int tOnHeight = mBitmapOn.getHeight();

		float tScaleOnWidth = ((float) (tOnWidth / 2)) / tOnWidth;
		float tScaleOnHeight = ((float) (tOnHeight / 2)) / tOnHeight;

		Matrix tMatrixOn = new Matrix();
		tMatrixOn.postScale(tScaleOnWidth, tScaleOnHeight);

		mBitmapOn = Bitmap.createBitmap(mBitmapOn, 0, 0, tOnWidth, tOnHeight,
				tMatrixOn, true);

		int tOffWidth = mBitmapOff.getWidth();
		int tOffHeight = mBitmapOff.getHeight();

		float tScaleOffWidth = ((float) (tOffWidth / 2)) / tOffWidth;
		float tScaleOffHeight = ((float) (tOffHeight / 2)) / tOffHeight;

		Matrix tMatrixOff = new Matrix();
		tMatrixOff.postScale(tScaleOffWidth, tScaleOffHeight);

		mBitmapOff = Bitmap.createBitmap(mBitmapOff, 0, 0, tOffWidth,
				tOffHeight, tMatrixOff, true);

		mOnOffWidthDif = (mBitmapOn.getWidth() - mBitmapOff.getWidth()) / 2;
		mOnOffHeightDif = (mBitmapOn.getHeight() - mBitmapOff.getHeight()) / 2;

		mImgMargin = mBitmapOn.getWidth() + DensityUtil.dip2px(getContext(), 5);
	}

	/**
	 * 设置总的点数
	 * 
	 * @param count
	 */
	public int setPointCount(int count) {
		mAllCount = count;

		return mScreenWidth - count * mImgMargin - mImgMargin * 3;
	}

	/**
	 * 设置当前需要选中的点索引
	 * 
	 * @param index
	 */
	public void setCurrentIndex(int index) {
		mCurrentIndex = index == 0 ? 0 : index % mAllCount;
		postInvalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
	}

	/**
	 * 绘制图片
	 */
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int tStartLeft = canvas.getWidth() - mAllCount * mImgMargin
				- mImgMargin;

		int tTop = (mViewHeight / 2) - (mBitmapOff.getHeight() / 2);
		int tTopOn = tTop - mOnOffHeightDif;

		for (int i = 0; i < mAllCount; i++) {

			if (i == mCurrentIndex) {

				int tLeftOn = (tStartLeft + (i + 1) * mImgMargin)
						- mOnOffWidthDif;

				canvas.drawBitmap(mBitmapOn, tLeftOn, tTopOn, mPaint);
			} else {

				int tLeftOff = tStartLeft + (i + 1) * mImgMargin;
				canvas.drawBitmap(mBitmapOff, tLeftOff, tTop, mPaint);
			}
		}
	}

}
