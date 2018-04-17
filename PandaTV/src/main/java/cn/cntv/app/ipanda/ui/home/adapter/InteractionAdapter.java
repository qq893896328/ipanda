package cn.cntv.app.ipanda.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.home.entity.Interaction;
import cn.cntv.app.ipanda.ui.home.listener.InterItemListener;
import cn.cntv.app.ipanda.utils.AutoUtils;
import cn.cntv.app.ipanda.utils.ViewHolder;

/**
 * @ClassName: InteractionAdapter
 * @author Xiao JinLai
 * @Date Jan 5, 2016 1:39:11 PM
 * @Description：Interaction adapter
 */
public class InteractionAdapter extends MyBaseAdapter {

	private List<Interaction> mDatas;
	private LayoutInflater mInflater;
	private InterItemListener mListener;

	public InteractionAdapter(Context context, List<Interaction> datas) {
		super(context);

		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
		this.mListener = new InterItemListener(context, mDatas);
	}

	@Override
	public int getCount() {
		return mDatas == null ? 0 : mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void updateData(List<Interaction> datas) {

		this.mDatas.clear();
		this.mDatas = datas;

		mListener.setData(mDatas);

		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.home_interactive_item,
					parent, false);
		}

		AutoUtils.autoSize(convertView);

		Interaction tInteraction = mDatas.get(position);

		LinearLayout tLlInterGroup = ViewHolder.get(convertView,
				R.id.llInterGroup);
		tLlInterGroup.setTag(position);
		tLlInterGroup.setOnClickListener(mListener);

		TextView tTvHomeGruop = ViewHolder.get(convertView,
				R.id.tvHomeInterTitle);
		tTvHomeGruop.setText(tInteraction.getTitle());

		ImageView tIvInterImg = ViewHolder
				.get(convertView, R.id.ivHomeInterImg);

		Glide.with(mContext)
				.load(tInteraction.getImage())
				.asBitmap()
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(tIvInterImg);

		return convertView;
	}

}
