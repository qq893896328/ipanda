package cn.cntv.app.ipanda.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.home.entity.SSVideoList;
import cn.cntv.app.ipanda.utils.AutoUtils;
import cn.cntv.app.ipanda.utils.ViewHolder;

/**
 * @ClassName: SSubjectList2Adapter
 * @author Xiao JinLai
 * @Date Jan 4, 2016 3:21:29 PM
 * @Description：Home special subject list2 adapter
 */
public class SSubjectList2Adapter extends BaseAdapter {

	private List<SSVideoList> mDatas;
	private LayoutInflater mInflater;
	private Context mContext;

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

	public SSubjectList2Adapter(LayoutInflater inflater,Context context,
			List<SSVideoList> datas) {

		this.mInflater = inflater;
		this.mDatas = datas;
		this.mContext = context;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.ssubject_list2_hlv_item,
					parent, false);
		}

		AutoUtils.autoSize(convertView);

		SSVideoList tVideoList = mDatas.get(position);

		ImageView tIvSSubItemBigImg = ViewHolder.get(convertView,
				R.id.ivSSubItemBigImg);

		Glide.with(mContext)
				.load(tVideoList.getImage())
				.asBitmap()
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(tIvSSubItemBigImg);

		RelativeLayout tRlPlayTime = ViewHolder.get(convertView,
				R.id.rlSSubItemPlayTime);

		String tVideoLength = tVideoList.getVideoLength();

		if (tVideoLength == null || tVideoLength.equals("")) {

			tRlPlayTime.setVisibility(View.GONE);
		} else {

			tRlPlayTime.setVisibility(View.VISIBLE);

			TextView tTvSSubItemPlayTime = ViewHolder.get(convertView,
					R.id.tvSSubItemPlayTime);
			tTvSSubItemPlayTime.setText(tVideoLength);
		}

		TextView tTvSSubItemTitle = ViewHolder.get(convertView,
				R.id.tvSSubItemTitle);
		tTvSSubItemTitle.setText(tVideoList.getTitle());

		return convertView;
	}
}
