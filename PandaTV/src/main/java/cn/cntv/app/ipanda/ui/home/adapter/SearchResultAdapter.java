package cn.cntv.app.ipanda.ui.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.home.entity.HITSBean;
import cn.cntv.app.ipanda.utils.ViewHolder;

/**
 * @author ma qingwei
 * @ClassName: SearchResultAdapter
 * @Date on 2016/6/7 16:24
 * @Description：
 */
public class SearchResultAdapter extends BaseAdapter {

    private List<HITSBean> mLists;
    private Context mContext;

    public SearchResultAdapter(Context context,List<HITSBean> lists){
        mLists = lists;
        mContext = context;
    }
    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int i) {
        return mLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.search_result_video_item,null);
        }
        ImageView iv1 = ViewHolder.get(view,R.id.pe_listview_item_one_img);
        TextView tv1 = ViewHolder.get(view,R.id.pe_listview_item_one_title);
        TextView tv2 = ViewHolder.get(view,R.id.pe_listview_item_one_time);
        TextView tv3 =ViewHolder.get(view,R.id.pe_listview_item_one_videotime);
        Glide.with(mContext)
                .load(mLists.get(i).getPICPATH())
                .asBitmap()
                .fitCenter()
                .placeholder(R.drawable.def_no_iv)
                .error(R.drawable.def_no_iv)
                .into(iv1);
        tv1.setText(mLists.get(i).getTITLE());
        tv2.setText(mLists.get(i).getPLAYTIME());
        tv3.setText(secToTime(mLists.get(i).getDURATION()));
        Log.i("info","--标题--"+mLists.get(i).getTITLE()+"--视频长度--"+mLists.get(i).getDURATION());
        return view;
    }


    // a integer to xx:xx:xx
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
