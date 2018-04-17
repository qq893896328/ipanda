package cn.cntv.app.ipanda.ui.home.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.home.entity.BigImg;

public class AutoScrollAdapter extends PagerAdapter {

	private List<BigImg> mBigImgs;

	private LayoutInflater mInflater;
	private OnClickListener mListener;

	private int mTitleWiedth;
	private int mPosition;
	private Context mContext;
	public AutoScrollAdapter(List<BigImg> bigImgs, Context context,
			OnClickListener listener, int titleWidth, int position) {

		this.mContext = context;
		this.mBigImgs = bigImgs;
		this.mInflater = LayoutInflater.from(context);
		this.mListener = listener;
		this.mTitleWiedth = titleWidth;
		this.mPosition = position;
	}

	@Override
	public int getCount() {
		return mBigImgs.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object o) {
		return view == o;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		BigImg tBigImg = mBigImgs.get(position);

		View tBannerView = mInflater.inflate(
				R.layout.view_banner_viewpager_item, container,false);

		ImageView tIvBigImg = (ImageView) tBannerView
				.findViewById(R.id.ivBigImg);
		tIvBigImg.setTag(mPosition + "," + position);
		tIvBigImg.setOnClickListener(mListener);

		Glide.with(mContext)
				.load(tBigImg.getImage())
				.asBitmap()
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(tIvBigImg);

		TextView tTvBigImgTitle = (TextView) tBannerView
				.findViewById(R.id.tvBigImgTitle);
		tTvBigImgTitle.setText(tBigImg.getTitle());
		tTvBigImgTitle.setWidth(mTitleWiedth);

		container.addView(tBannerView);

		return tBannerView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
