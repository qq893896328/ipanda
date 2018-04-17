package cn.cntv.app.ipanda.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.cntv.app.ipanda.R;

/**
 * @author ma qingwei
 * @ClassName: PopDialogUtils
 * @Date on 2016/6/16 12:21
 * @Description：提示对话框样式
 */
public class PopDialogUtils {

    private Context mContext;

    public PopDialogUtils(Context context){
        mContext = context;
    }

    /**
     * 两个按钮的对话框
     *  “确定” “取消” 样式的对话框
     * @param parent
     * @param contentId 提示内容id
     * @param sureId    确定string id
     * @param cancelId  取消string id
     * @return
     */
    public PopupWindow showTipPop1(final View parent, int contentId, int sureId, int cancelId, View.OnClickListener listener) {

         PopupWindow tPop;

         View dialogView = LayoutInflater.from(mContext).inflate(R.layout.player_tip, null, false);

         tPop = new PopupWindow(dialogView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
         tPop.setFocusable(true);//创建弹出的对话框

         TextView tipTv = (TextView) dialogView.findViewById(R.id.tv_tip);
         tipTv.setText(contentId);//提示内容

         TextView okbtn = (TextView) dialogView.findViewById(R.id.btn_ok);
         okbtn.setText(sureId);//确定按钮
         okbtn.setOnClickListener(listener);

         TextView cancelbtn = (TextView) dialogView.findViewById(R.id.btn_cancel);
         cancelbtn.setText(cancelId);//取消按钮
         cancelbtn.setOnClickListener(listener);

         tPop.showAtLocation(parent, Gravity.TOP | Gravity.CENTER, 0, 0);

         return  tPop;
    }

    /**
     * 一个按钮的对话框
     * “我知道了” 等样式的对话框
     * @param parent
     * @param contentId
     * @param sureId
     * @param listener
     * @return
     */
    public PopupWindow showTipPop2(final View parent, String color,int contentId, int sureId, View.OnClickListener listener) {

        PopupWindow tPop;

        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.player_tip, null, false);

        tPop = new PopupWindow(dialogView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        tPop.setFocusable(true);//创建弹出的对话框

        TextView tipTv = (TextView) dialogView.findViewById(R.id.tv_tip);
        tipTv.setText(contentId);//提示内容

        TextView okbtn = (TextView) dialogView.findViewById(R.id.btn_ok);
        okbtn.setText(sureId);//确定按钮
        okbtn.setTextColor(Color.parseColor(color));
        okbtn.setOnClickListener(listener);

        TextView cancelbtn = (TextView) dialogView.findViewById(R.id.btn_cancel);
        cancelbtn.setVisibility(View.GONE);

        LinearLayout layout = (LinearLayout) dialogView.findViewById(R.id.ll_tips_line);
        layout.setVisibility(View.GONE);

        tPop.showAtLocation(parent, Gravity.TOP | Gravity.CENTER, 0, 0);
        return  tPop;
    }


    /**
     * 两行提示文字
     * 搜索样式对话框
     * @param parent
     * @param contentId
     * @param sureId
     * @param listener
     * @return
     */
    public PopupWindow showTipPop3(final View parent, int contentId, int sureId, View.OnClickListener listener) {

        PopupWindow tPop;

        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.common_ppw1, null, false);

        tPop = new PopupWindow(dialogView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        tPop.setFocusable(true);//创建弹出的对话框

        TextView tipTv = (TextView) dialogView.findViewById(R.id.tv_tip2);
        tipTv.setText(contentId);//提示内容

        TextView okbtn = (TextView) dialogView.findViewById(R.id.btn_ok);
        okbtn.setText(sureId);//确定按钮
        okbtn.setOnClickListener(listener);

        TextView cancelbtn = (TextView) dialogView.findViewById(R.id.btn_cancel);
        cancelbtn.setVisibility(View.GONE);

        tPop.showAtLocation(parent, Gravity.TOP | Gravity.CENTER, 0, 0);
        return  tPop;
    }


}
