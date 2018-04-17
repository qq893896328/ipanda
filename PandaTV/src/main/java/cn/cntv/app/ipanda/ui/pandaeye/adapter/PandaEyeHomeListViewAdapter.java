package cn.cntv.app.ipanda.ui.pandaeye.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.pandaeye.entity.PEListDetail;
import cn.cntv.app.ipanda.utils.AutoUtils;
import cn.cntv.app.ipanda.utils.PEDPUtils;

public class PandaEyeHomeListViewAdapter extends BaseAdapter {
    
	private List<PEListDetail> mList;
	private Context mContext;
    
	public PandaEyeHomeListViewAdapter(Context context, List<PEListDetail> list) {

		this.mList = list;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
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

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.pandaeye_listview_item_one, null);
			holder.result_img = (ImageView) convertView.findViewById(R.id.pe_listview_item_one_img);
			holder.title = (TextView) convertView.findViewById(R.id.pe_listview_item_one_title);
			holder.time = (TextView) convertView.findViewById(R.id.pe_listview_item_one_time);
			//holder.type = (TextView) convertView.findViewById(R.id.pe_listview_item_one_type);
			holder.timeFrame = (RelativeLayout) convertView.findViewById(R.id.pe_listview_item_one_relayout);
			holder.videoTime = (TextView) convertView.findViewById(R.id.pe_listview_item_one_videotime);
			holder.imgFrame = (FrameLayout) convertView.findViewById(R.id.pe_listview_item_one_frame);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
//		if(list.get(position).equals("two")){
//			holder.result_img.setVisibility(View.GONE);
//		}else{
//		}
		
		holder.result_img.setVisibility(View.VISIBLE);
		LayoutParams params = holder.result_img.getLayoutParams();
		params.height = PEDPUtils.dip2px(mContext, 77);
		params.width = PEDPUtils.dip2px(mContext, 77) / 9 * 16;
		   
		
		
		
		if(mList.get(position).getDatatype().trim().equals("video")){
			holder.timeFrame.setVisibility(View.VISIBLE);
			if(null == mList.get(position).getVideolength() && mList.get(position).getVideolength().trim().length() == 0){
				
				holder.videoTime.setText("");
			}else{
				    
				String videoTimeLength = mList.get(position).getVideolength().trim();
				
//				videoTimeLength = videoTimeLength.substring(3,videoTimeLength.length());
				
				holder.videoTime.setText(videoTimeLength);
			}
		}else{
			//图文
			holder.timeFrame.setVisibility(View.GONE);
		}
		
		
		
		
		
		if(null == mList.get(position).getTitle()){
			
			holder.title.setText("");
		}else{
			
			holder.title.setText(mList.get(position).getTitle());
		}
		
		
		if(null == mList.get(position).getFocus_date()){
			holder.time.setText("");
		}else{
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String date = sdf.format(new Date(*1000L));
			
			holder.time.setText(convert(Long.parseLong(mList.get(position).getFocus_date())));
		}
		
		
		//holder.type.setText("微时评");
		
		
		if(null == mList.get(position).getPicurl() || mList.get(position).getPicurl().trim().length() == 0){
			
			//没图片地址
			if(mList.get(position).getDatatype().trim().equals("video")){
				Glide.with(mContext)
						.load("")
						.asBitmap()
						.placeholder(R.drawable._no_img)
						.error(R.drawable._no_img)
						.into(holder.result_img);
				holder.imgFrame.setVisibility(View.VISIBLE);
			}else{
				//图文的把图片隐藏了
				holder.imgFrame.setVisibility(View.GONE);
			}
			
			
			
		}else{
			
			holder.imgFrame.setVisibility(View.VISIBLE);

			Glide.with(mContext)
					.load(mList.get(position).getPicurl())
					.asBitmap()
					.placeholder(R.drawable._no_img)
					.error(R.drawable._no_img)
					.into(holder.result_img);
		}
		
		AutoUtils.autoSize(convertView);

		return convertView;

	}
	
	
	public String convert(long mill){
		Date date=new Date(mill);
		String strs="";
		try {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		strs=sdf.format(date);
		} catch (Exception e) {
		e.printStackTrace();
		}
		return strs;
		}
	
	
	

	class ViewHolder {

		private ImageView result_img;

		private TextView title, time;
		private RelativeLayout timeFrame;
		private TextView videoTime;
		private FrameLayout imgFrame;

	}

}
