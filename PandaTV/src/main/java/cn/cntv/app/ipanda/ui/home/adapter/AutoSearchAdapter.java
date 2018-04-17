package cn.cntv.app.ipanda.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.home.entity.HITSBean;
import cn.cntv.app.ipanda.utils.ViewHolder;


/**
 * @author ma qingwei
 * @ClassName: AutoSearchAdapter
 * @Date on 2016/6/6 14:49
 * @Description：
 */
public class AutoSearchAdapter extends BaseAdapter {

    private Context mContext;
    private List<HITSBean> mAutoList;

    public  AutoSearchAdapter(Context context, List<HITSBean> list){

        mContext = context;
        mAutoList = list;
    }
    @Override
    public int getCount() {
        return mAutoList.size();
    }

    @Override
    public Object getItem(int i) {
        return mAutoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.auto_search_listview_item,null);

        }
            TextView view1 = ViewHolder.get(view, R.id.tv_auto_search_title);
            view1.setText(mAutoList.get(i).getTITLE());


        return view;
    }
}
