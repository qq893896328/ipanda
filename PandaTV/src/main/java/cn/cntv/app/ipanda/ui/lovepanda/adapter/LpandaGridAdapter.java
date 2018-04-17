package cn.cntv.app.ipanda.ui.lovepanda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gridsum.mobiledissector.util.StringUtil;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaColimnList;

public class LpandaGridAdapter extends LpandaBaseAdapter {
	//	private List<Map<String, Object>> data;
	private List<LpandaColimnList> data;
	private LayoutInflater mInflater;
	private Boolean haveScrollbar;
	public LpandaGridAdapter(Context context,List<LpandaColimnList> data)
	{
		//根据context上下文加载布局，这里的是Demo17Activity本身，即this
		this.mInflater = LayoutInflater.from(context);
		this.data=data;
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
	class ViewHolder{
		TextView title;
		ImageView image;

	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		LpandaColimnList entity= data.get(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.lpanda_grid_itm, null);
			holder.title= (TextView) convertView.findViewById(R.id.lpanda_griditem_name);
			holder.image= (ImageView) convertView.findViewById(R.id.lpanda_griditem_image);

			convertView.setTag(holder);

		}else{
			holder= (ViewHolder) convertView.getTag();
		}
		 setInfo(position, entity, holder, convertView);
		

		return convertView;
	}
	 public void setHaveScrollbar(boolean haveScrollbar) {   
	        this.haveScrollbar = haveScrollbar;   
	    }   

	private void setInfo(int position, LpandaColimnList column,
			ViewHolder holder, View convertView) {
		holder.title.setText(column.getTitle());
		holder.image.setScaleType(StringUtil.isNullOrEmpty(column
				.getImage()) ? ScaleType.FIT_XY : ScaleType.CENTER_CROP);

		Glide.with(mContext)
				.load(column.getImage())
				.asBitmap()
				.placeholder(R.drawable._row1_3)
				.error(R.drawable._row1_3)
				.into(holder.image);

	}
}
