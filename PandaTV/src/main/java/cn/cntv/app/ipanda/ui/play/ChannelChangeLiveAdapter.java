package cn.cntv.app.ipanda.ui.play;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.play.entity.PlayLiveEntity;

public class ChannelChangeLiveAdapter extends BaseAdapter{
	List<PlayLiveEntity> data;
	private Context context;
	private LayoutInflater mInflater ;
	
	public ChannelChangeLiveAdapter(Context context){
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public void setData(List<PlayLiveEntity> data) {
		this.data = data;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.popupwindow_channel_change_live_item, parent, false);
			holder = new ViewHolder();
			
			holder.title = (TextView) convertView.findViewById(R.id.change_channel_type);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(data.get(position).getTitle());
		
		
		if(data.get(position).getSign() == 1){
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
