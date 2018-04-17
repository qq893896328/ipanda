package cn.cntv.app.ipanda.ui.play;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.cctv.entity.CCTVVideo;

public class ChannelChangeVodAdapter extends BaseAdapter{
	List<CCTVVideo> listVideos;
	private Context context;
	private LayoutInflater mInflater ;
	
	public ChannelChangeVodAdapter(Context context,List<CCTVVideo> listVideos){
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listVideos = listVideos;
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listVideos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listVideos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.popupwindow_channel_change_vod_item, parent, false);
			holder = new ViewHolder();
			
			holder.title = (TextView) convertView.findViewById(R.id.change_channel_type);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(null != listVideos.get(position).getT()){
			
			holder.title.setText(listVideos.get(position).getT());
		}else{
			holder.title.setText("");
			
		}
		
		if(listVideos.get(position).getSign() == 1){
			
			holder.title.setBackgroundColor(context.getResources().getColor(R.color.media_qingxidu));
		}else{
			holder.title.setBackgroundColor(context.getResources().getColor(R.color.transparent));
		}
		
		
		
		return convertView;
	}
	
	class ViewHolder{
		TextView title;
	}

}
