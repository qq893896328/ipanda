package cn.cntv.app.ipanda.ui.lovepanda.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gridsum.mobiledissector.util.StringUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.lovepanda.entity.VideoBean;

public class LpandaColumnCatAdapter extends LpandaBaseAdapter {
    private List<VideoBean> data;
    private LayoutInflater mInflater;
    private Context context;

    public LpandaColumnCatAdapter(Context context, List<VideoBean> data) {
        //根据context上下文加载布局，这里的是Demo17Activity本身，即this
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
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
        TextView title, time, length;
        ImageView image;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        VideoBean entity = data.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.lpanda_list_item, null, false);
            holder.title = (TextView) convertView.findViewById(R.id.lpanda_demand_title);
            holder.image = (ImageView) convertView.findViewById(R.id.lpanda_demand_live);
            holder.time = (TextView) convertView.findViewById(R.id.lpanda_demand_data);
            holder.length = (TextView) convertView.findViewById(R.id.lpanda_videoLength);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        setInfo(position, entity, holder, convertView);
        return convertView;
    }

    private void setInfo(int position, VideoBean column,
                         ViewHolder holder, View convertView) {
        // TODO Auto-generated method stub
        holder.image.setImageDrawable(null);
        holder.image.setScaleType(StringUtil.isNullOrEmpty(column
                .getImg()) ? ScaleType.FIT_XY : ScaleType.CENTER_CROP);

        Glide.with(mContext)
                .load(column.getImg())
                .asBitmap()
                .placeholder(R.drawable.def_no_iv)
                .error(R.drawable.def_no_iv)
                .into(holder.image);
        holder.title.setText(column.getT().trim());
        if (null == column.getPtime()) {
            holder.time.setText("");
        } else {
            holder.time.setText(convert(column.getPtime(),column,holder));

        }
        holder.length.setText(column.getLen());
    }

    /**
     * 将日期格式的字符串 转换成时间戳格式
     * @param date
     * @return
     */
    public String convert(String date, VideoBean column, ViewHolder holder) {

        Date tdate = null;
        SimpleDateFormat sdf1 = null;
        SimpleDateFormat sdf = null;
        String   strDate = date.trim();

        try {

            if (strDate.length() >= 19) {//用字符串长度判断时间格式,最长则不会超过年月日时分秒格式长度
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            } else {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            }

            tdate = sdf.parse(strDate);
            sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        } catch (ParseException e) {
            holder.time.setText(column.getPtime());
        }
        return sdf1.format(tdate);

    }

}
