package cn.cntv.app.ipanda.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.utils.ViewHolder;


/**
 * @author ma qingwei
 * @ClassName: HistoryWordsAdapter
 * @Date on 2016/6/7 12:02
 * @Description：
 */
public class HistoryWordsAdapter extends BaseAdapter {

    private Context mContext;
    private String [] mWords;

    public HistoryWordsAdapter(Context context, String[] words){
        mContext = context;
        mWords = words;
    }
    @Override
    public int getCount() {
        return mWords.length;
    }

    @Override
    public Object getItem(int i) {
        return mWords[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.search_history_words_grid_item,null);

        }
        TextView hisText = ViewHolder.get(view,R.id.hot_text_view);
        if(!mWords[i].isEmpty())
        hisText.setText(mWords[i]);
        return view;
    }
}
