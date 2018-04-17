package cn.cntv.app.ipanda.ui.personal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.db.entity.PlayHistoryEntity;
import cn.cntv.app.ipanda.utils.AutoUtils;
import cn.cntv.app.ipanda.utils.PEDPUtils;
import cn.cntv.app.ipanda.utils.TimeUtil;

public class PersonalHistoryListViewAdapter extends BaseAdapter {

	private List<PlayHistoryEntity> list;
	private Context context;

	public PersonalHistoryListViewAdapter(Context context, List<PlayHistoryEntity> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		if(null != list){
			if(list.size() > 50){
				return 50;
			}else{
				
				return list.size();
			}
		}else{
			return 0;
		}
		
	}

	@Override
	public PlayHistoryEntity getItem(int position) {
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
					R.layout.personal_history_listview_item, null);
			holder.stateImg = (ImageView) convertView.findViewById(R.id.personal_hy_state_img);
			holder.itemImg = (ImageView) convertView.findViewById(R.id.personal_hy_item_img);
			holder.title = (TextView) convertView.findViewById(R.id.personal_hy_listview_title);
//			holder.itemLayout = (RelativeLayout) convertView.findViewById(R.id.personal_hy_item_layout);
			holder.time = (TextView) convertView.findViewById(R.id.personal_hy_listview_time);
			holder.videoLength = (TextView) convertView.findViewById(R.id.personal_hy_listview_videoLength);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		PlayHistoryEntity playHistory = getItem(position);
		if(playHistory.getSign() == null || playHistory.getSign() == 0){
			holder.stateImg.setVisibility(View.GONE);
		}else{
			holder.stateImg.setVisibility(View.VISIBLE);
		}

		if(!TextUtils.isEmpty(playHistory.getTitle()))
		holder.title.setText(playHistory.getTitle());
		
		if(0!=(playHistory.getPlaytime()))
		holder.time.setText(/*convert(Long.parseLong(phm.getPlaytime()))*/new SimpleDateFormat("yyyy-MM-dd HH:mm").format(playHistory.getPlaytime()));

		if(!TextUtils.isEmpty(playHistory.getTimeLenth()))
		holder.videoLength.setText(/*convert(Long.parseLong(phm.getTimeLenth()))*/TimeUtil.getTime3(playHistory.getTimeLenth()));

		if(playHistory.getIscheck() == null || !playHistory.getIscheck()){
			holder.stateImg.setImageResource(R.drawable.personal_normal_img);
		}else{
			holder.stateImg.setImageResource(R.drawable.personal_check_img);
		}

		LayoutParams params = holder.itemImg.getLayoutParams();
		params.height = PEDPUtils.dip2px(context, 77);
		params.width = PEDPUtils.dip2px(context, 77) / 9 * 16;

		if(null != playHistory.getVideoImg()){
			Glide.with(context)
					.load(playHistory.getVideoImg().trim())
					.placeholder(R.drawable._no_img)
					.error(R.drawable._no_img)
					.into(holder.itemImg);
		}else{
			Glide.with(context)
					.load("")
					.placeholder(R.drawable._no_img)
					.error(R.drawable._no_img)
					.into(holder.itemImg);
		}

//		if(null == list.get(position).getVideoinfo()){
//			holder.title.setText(list.get(position).getTitle());
//			if(null == list.get(position).getPlaytime()){
//				
//				holder.time.setText("");
//			}else{
//				
//				holder.time.setText(convert(Long.parseLong(list.get(position).getPlaytime())));
//			}
//			
//			if(!list.get(position).isIscheck()){
//				holder.stateImg.setImageResource(R.drawable.personal_normal_img);
//			}else{
//				holder.stateImg.setImageResource(R.drawable.personal_check_img);
//			}			
//			
////			holder.itemLayout.setOnClickListener(new OnClickListener() {
////				@Override
////				public void onClick(View arg0) {
////					
//////					if(list.get(position).getSign() == 0){
//////						holder.stateImg.setImageResource(R.drawable.personal_normal_img);
//////					}else{
//////						holder.stateImg.setImageResource(R.drawable.personal_check_img);
//////					}
////					
////					if(list.get(position).isIscheck()){
////						holder.stateImg.setImageResource(R.drawable.personal_normal_img);
////						list.get(position).setIscheck(false);
////					}else{
////						list.get(position).setIscheck(true);
////						holder.stateImg.setImageResource(R.drawable.personal_check_img);
////					}
////				}
////			});
//			
//			
//			if(null != list.get(position).getVideoimg()){
//				
//				imageLoader.displayImage(list.get(position).getVideoimg().trim(), holder.itemImg,mOptions);
//
//			}else{
//				imageLoader.displayImage("", holder.itemImg,mOptions);
//			}
//		}else{
//			
//			holder.title.setText(list.get(position).getVideoinfo().getTitle());
//			if(null == list.get(position).getVideoinfo().getPlaytime()){
//				
//				holder.time.setText("");
//			}else{
//				
//				holder.time.setText(convert(Long.parseLong(list.get(position).getVideoinfo().getPlaytime())));
//			}
//			
//			if(!list.get(position).isIscheck()){
//				holder.stateImg.setImageResource(R.drawable.personal_normal_img);
//			}else{
//				holder.stateImg.setImageResource(R.drawable.personal_check_img);
//			}			
//			
////			holder.itemLayout.setOnClickListener(new OnClickListener() {
////				@Override
////				public void onClick(View arg0) {
////					
//////					if(list.get(position).getSign() == 0){
//////						holder.stateImg.setImageResource(R.drawable.personal_normal_img);
//////					}else{
//////						holder.stateImg.setImageResource(R.drawable.personal_check_img);
//////					}
////
////					if(list.get(position).isIscheck()){
////						holder.stateImg.setImageResource(R.drawable.personal_normal_img);
////						list.get(position).setIscheck(false);
////					}else{
////						list.get(position).setIscheck(true);
////						holder.stateImg.setImageResource(R.drawable.personal_check_img);
////					}
////				}
////			});
//			
//			
//			if(null != list.get(position).getVideoimg()){
//				
//				imageLoader.displayImage(list.get(position).getVideoinfo().getVideoimg().trim(), holder.itemImg,mOptions);
//
//			}else{
//				imageLoader.displayImage("", holder.itemImg,mOptions);
//			}
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
//		
//		
//		
//		LayoutParams params = holder.itemImg.getLayoutParams();
//		params.height = PEDPUtils.dip2px(context, 77);
//		params.width = PEDPUtils.dip2px(context, 77) / 9 * 16;
//		
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

		AutoUtils.autoSize(convertView);

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
