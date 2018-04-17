package cn.cntv.app.ipanda.ui.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.cntv.app.ipanda.R;


public class PersonalFKGridAdapter extends BaseAdapter {
	// 搜索结果界面的gridview的adapter
	private Context context;
	private List<String> list_gridview;
	private LayoutInflater inflater;
	
	private int displayHeight;
	private int displayWidth;
	
	

	public PersonalFKGridAdapter(Context context,
			List<String> list_gridview,int displayWidth,int displayHeight) {
		this.context = context;
		this.list_gridview = list_gridview;
		inflater = LayoutInflater.from(context);
		this.displayHeight = displayHeight;
        this.displayWidth = displayWidth;
        
	}

	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_gridview.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_gridview.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;   
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.personal_set_fankui_grid_item, parent, false);
			holder.result_img = (TextView) convertView
					.findViewById(R.id.personal_set_fk_grid_state);
			holder.result_title = (TextView) convertView
					.findViewById(R.id.personal_set_fk_grid_content);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		holder.result_title.setText(list_gridview.get(position));
		
		
//		holder.result_title.setText(list_gridview.get(position).getTitle());
//		holder.result_img.setScaleType(ScaleType.FIT_XY);
//		holder.result_img.setLayoutParams(new RelativeLayout.LayoutParams(
//				(displayHeight - 20) / 2, ((displayWidth - 20) / 2) * 9 / 16));

		return convertView;
	}

	class ViewHolder {
		private TextView result_img;
		private TextView result_title;
	}

}

