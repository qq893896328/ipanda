package cn.cntv.app.ipanda.view;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import cn.cntv.app.ipanda.R;

/**
 * @author Xiao JinLai
 * @ClassName: FragmentDialogUtils
 * @Date 2016-2-21 上午10:39:08
 * @Description：
 */
@SuppressLint("NewApi")
public class VoteDialog extends DialogFragment {

    private String mTitle;

    public VoteDialog() {
    }

    @SuppressLint("ValidFragment")
    public VoteDialog(String title) {

        this.mTitle = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View tView = inflater.inflate(R.layout.vote_dialog, container);

        TextView tTvTitle = (TextView) tView
                .findViewById(R.id.tvVoteDialogTitle);
        tTvTitle.setText(mTitle);

        tView.findViewById(R.id.tvVoteDialogOk).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dismiss();
                    }
                });

        return tView;
    }
}
