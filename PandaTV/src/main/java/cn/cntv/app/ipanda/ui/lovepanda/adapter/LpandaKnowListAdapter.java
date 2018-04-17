package cn.cntv.app.ipanda.ui.lovepanda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaColimnList;
import cn.cntv.app.ipanda.utils.AutoUtils;

public class LpandaKnowListAdapter extends LpandaBaseAdapter {
	private List<LpandaColimnList> data;
	private LayoutInflater mInflater;

	public LpandaKnowListAdapter(Context context, List<LpandaColimnList> data) {
		// 根据context上下文加载布局，这里的是Demo17Activity本身，即this
		this.mInflater = LayoutInflater.from(context);
		this.data = data;
		setmContext(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class ViewHolder {
		TextView title;
		ImageView image;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LpandaColimnList entity = data.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
//			convertView = mInflater.inflate(R.layout.lpanda_know_list_item, null,
//					false);
			convertView = mInflater.inflate(R.layout.lpanda_grid_one, null,
					false);
			holder.title = (TextView) convertView
					.findViewById(R.id.lpanda_listitem_name_one);
			holder.image = (ImageView) convertView
					.findViewById(R.id.lpanda_listitem_image_one);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		setInfo(position, entity, holder, convertView);
		AutoUtils.auto(convertView);
		return convertView;
	}

	private void setInfo(int position, LpandaColimnList column,
			ViewHolder holder, View convertView) {
		// TODO Auto-generated method stub
		// holder.image.setImageDrawable(null);
		// holder.image.setScaleType(StringUtil.isNullOrEmpty(column
		// .getImage()) ? ScaleType.FIT_XY : ScaleType.CENTER_CROP);
		Glide.with(mContext)
				.load(column.getImage())
				.asBitmap()
				.placeholder(R.drawable.def_no_iv)
				.error(R.drawable.def_no_iv)
				.into(holder.image);
		holder.title.setText(column.getTitle());
	}

}
