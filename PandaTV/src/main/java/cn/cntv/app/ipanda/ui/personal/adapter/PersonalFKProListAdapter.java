package cn.cntv.app.ipanda.ui.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.cntv.app.ipanda.R;

public class PersonalFKProListAdapter extends BaseAdapter {

	private Context context;
	private List<String> list;
	
	public PersonalFKProListAdapter(Context context,List<String> list){
		this.context = context;
		this.list = list;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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

			convertView = LayoutInflater.from(context).inflate(
					R.layout.personal_fk_pro_list_item, null);
//			holder.stateImg = (ImageView) convertView.findViewById(R.id.personal_hy_state_img);
//			holder.itemImg = (ImageView) convertView.findViewById(R.id.personal_hy_item_img);
//			holder.title = (TextView) convertView.findViewById(R.id.personal_hy_listview_title);
//			holder.itemLayout = (RelativeLayout) convertView.findViewById(R.id.personal_hy_item_layout);
//			holder.time = (TextView) convertView.findViewById(R.id.personal_hy_listview_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
//		if(list.get(position).getSign() == 0){
//			holder.stateImg.setVisibility(View.GONE);
//		}else{
//			holder.stateImg.setVisibility(View.VISIBLE);
//		}
		
		
		
//		holder.itemImg.setLayoutParams(new RelativeLayout.LayoutParams(
//		(displayHeight - 20) / 2, ((displayWidth - 20) / 2) * 9 / 16));
		
		
//		if(list.get(position).equals("two")){
//			holder.result_img.setVisibility(View.GONE);
//		}else{
//			holder.result_img.setVisibility(View.VISIBLE);
//			LayoutParams params = holder.result_img.getLayoutParams();
//			params.height = PEDPUtils.dip2px(context, 77);
//			params.width = PEDPUtils.dip2px(context, 77) / 9 * 16;
//		}
		
		
		
//		LayoutParams params = holder.itemImg.getLayoutParams();
//		params.height = PEDPUtils.dip2px(context, 77);
//		params.width = PEDPUtils.dip2px(context, 77) / 9 * 16;
		
//		if(null == list.get(position).getItemTitle()){
//			
//			holder.title.setText("");
//		}else{
//			
//			holder.title.setText(list.get(position).getItemTitle());
//		}
//		
//		
//		if(null == list.get(position).getPubDate()){
//			holder.time.setText("");
//		}else{
////			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////			String date = sdf.format(new Date(*1000L));
//			
//			holder.time.setText(convert(Long.parseLong(list.get(position).getPubDate())));
//		}
//		
//		
//		holder.type.setText("微时评");
//		
//		
//		mImageLoader.displayImage(list.get(position).getItemImage().getImgUrl1(), holder.result_img,mOptions);
//		
		

//		holder.result_title.setText(list_gridview.get(position).getTitle());
//
//		holder.result_img.setScaleType(ScaleType.FIT_XY);
//
//		fb.display(holder.result_img, list_gridview.get(position)
//				.getVideoPicAdd());
//
//		holder.result_img.setLayoutParams(new RelativeLayout.LayoutParams(
//
//		(displayHeight - 20) / 2, ((displayWidth - 20) / 2) * 9 / 16));

		return convertView;

	}
	
	
	public String convert(long mill){
		Date date=new Date(mill);
		String strs="";
		try {
		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd-yyyy hh:mm");
		strs=sdf.format(date);
		} catch (Exception e) {
		e.printStackTrace();
		}
		return strs;
		}
	
	
	

	class ViewHolder {

		private ImageView stateImg,itemImg;

		private RelativeLayout itemLayout;
		private TextView videoLength,title, time;

	}


}
