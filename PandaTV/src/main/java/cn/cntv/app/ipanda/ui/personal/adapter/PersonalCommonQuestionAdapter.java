package cn.cntv.app.ipanda.ui.personal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.ui.personal.entity.CommonQuestionModle;

public class PersonalCommonQuestionAdapter extends BaseAdapter {

    private List<CommonQuestionModle> list;
    private Context context;

    public PersonalCommonQuestionAdapter(Context context,
                                         List<CommonQuestionModle> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
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
                    R.layout.activity_personal_feedback_commonquestion_item,
                    null);
            holder.title = (TextView) convertView
                    .findViewById(R.id.commonquestion_item_title);
            holder.content = (TextView) convertView
                    .findViewById(R.id.commonquestion_item_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CommonQuestionModle cqm = (CommonQuestionModle) getItem(position);
        // holder.time.setText(convert(Long.parseLong(list.get(position).getPlaytime())));
        holder.title.setText(cqm.getTitle().contains("？") ? cqm.getTitle().replace("？", "") : cqm.getTitle());
        holder.content.setText(cqm.getConten().trim());
        return convertView;

    }

    public String convert(long mill) {
        Date date = new Date(mill);
        String strs = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm");
            strs = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }

    class ViewHolder {
        private TextView videoLength, title, time, content;
    }

}
