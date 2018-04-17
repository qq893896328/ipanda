package cn.cntv.app.ipanda.ui.lovepanda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.lovepanda.entity.LpandaTalkContentInfo;

public class LpandaTalkAdapter extends LpandaBaseAdapter {

	private List<LpandaTalkContentInfo> talkData;
	private LayoutInflater mInflater;
	private int commentCount;
	public LpandaTalkAdapter(Context context,List<LpandaTalkContentInfo> talkData){
		//根据context上下文加载布局，这里的是Demo17Activity本身，即this
		this.mInflater = LayoutInflater.from(context);
		this.talkData=talkData;
		setmContext(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return talkData.size();
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
	@Override
	public void addItems(List items) {
		// TODO Auto-generated method stub
		talkData.add((LpandaTalkContentInfo) items);
	}
	class ViewHolder{ 
		TextView uid,content,count,data;
	}
	LpandaTalkContentInfo entity;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		entity= talkData.get(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.lpanda_watchtalk_listitem, null,false);
			holder.uid= (TextView) convertView.findViewById(R.id.lpanda_watchtalk_uid);
			holder.content= (TextView) convertView.findViewById(R.id.lpanda_watchtalk_content);
			holder.count= (TextView) convertView.findViewById(R.id.lpanda_watchtalk_count);
			holder.data=(TextView) convertView.findViewById(R.id.lpanda_watchtalk_data);

			convertView.setTag(holder);

		}else{
			holder= (ViewHolder) convertView.getTag();
		}
		setInfo(position, entity, holder, convertView);
		return convertView;
	}
	String floor;
	private void setInfo(int position, LpandaTalkContentInfo column,
			ViewHolder holder, View convertView) {

		holder.uid.setText(column.getAuthor());
		holder.content.setText(column.getMessage());
		//实现倒序排列
//		holder.count.setText((getCount()-position)+" 楼");
		holder.count.setText((commentCount-position)+mContext.getString(R.string.floor));
		holder.data.setText(TimeStamp2Date(column.getDateline(), "MM-dd-yyyy"));
	}
	
	public void setCommentCount(int commentCount){
		this.commentCount = commentCount;
	}
	
	/**
	 * unix时间戳 转换成时间格式
	 *  wrp
	 * @return
	 */
	public String TimeStamp2Date(String timestampString, String formats){    
		Long timestamp = Long.parseLong(timestampString)*1000;    
		String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));    
		return date;    
	}  
}
