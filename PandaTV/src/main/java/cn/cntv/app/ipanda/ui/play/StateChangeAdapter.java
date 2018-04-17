package cn.cntv.app.ipanda.ui.play;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.play.entity.StateEntity;

public class StateChangeAdapter extends BaseAdapter{
	List<StateEntity> data;
	private Context context;
	private LayoutInflater mInflater ;
	
	public StateChangeAdapter(Context context,List<StateEntity> data){
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
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
			convertView = mInflater.inflate(R.layout.pop_sate_change_item, parent, false);
			holder = new ViewHolder();
			
			holder.title = (TextView) convertView.findViewById(R.id.pop_state_item_view);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.title.setText(data.get(position).getTitle());
		
		
//		if(data.get(position).getCheck().equals("yes")){
//			
//		}
		
		
		
		
		return convertView;
	}
	
	class ViewHolder{
		TextView title;
	}

}
