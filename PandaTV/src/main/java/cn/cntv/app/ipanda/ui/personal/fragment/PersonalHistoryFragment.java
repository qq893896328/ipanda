package cn.cntv.app.ipanda.ui.personal.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gridsum.mobiledissector.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.constant.CollectTypeEnum;
import cn.cntv.app.ipanda.custom.HandlerMessage;
import cn.cntv.app.ipanda.custom.XjlHandler;
import cn.cntv.app.ipanda.db.DBInterface;
import cn.cntv.app.ipanda.db.entity.PlayHistoryEntity;
import cn.cntv.app.ipanda.listener.HandlerListener;
import cn.cntv.app.ipanda.ui.BaseActivity;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.personal.activity.PersonalActivity;
import cn.cntv.app.ipanda.ui.personal.activity.PersonalHistoryActivity;
import cn.cntv.app.ipanda.ui.personal.adapter.PersonalHistoryListViewAdapter;
import cn.cntv.app.ipanda.ui.play.PlayVodFullScreeActivity;
import cn.cntv.app.ipanda.ui.play.entity.PlayVodEntity;

/**
 * Created by ASUS on 2016/7/28.
 */
public class PersonalHistoryFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private TextView titleLeft, titleCenter, titleRight;
    private TextView allView, deleteView;
    private LinearLayout historyLayout;
    private int editSign, allSign, delSign;

    private List<PlayHistoryEntity> listVal;
    private PersonalHistoryListViewAdapter pAdapter;

    private RelativeLayout dataLayout;
    private ImageView netImg;

    private DBInterface dbInterface = DBInterface.getInstance();

    private static final int LIST_DATA1 = 1;
    private View view;

    private XjlHandler mHandler1 = new XjlHandler(new HandlerListener() {

        @Override
        public void handlerMessage(HandlerMessage msg) {

            dismissLoadDialog();

            switch (msg.what) {
                case Integer.MAX_VALUE:
                    // 错误处理
                    Log.e("wang", msg.obj.toString());
                    break;
                case LIST_DATA1:
                    PIHyData data1 = (PIHyData) msg.obj;
                    dataLayout.setVisibility(View.VISIBLE);
                    netImg.setVisibility(View.GONE);
                    Log.e("test", "history=" + data1.toString());

                    if (null != data1) {
                        // listVal = data1.getList();

                        final List<PlayHistoryEntity> list =
                                dbInterface.getPlayHistoryOrderByPlayTime();
                        if (null == list) {
                            dbInterface.batchInsertOrUpdatePlayHistory(data1.getList());
                            listVal.addAll(data1.getList());
                        } else
                            listVal.addAll(list);
                        // pAdapter = new
                        // PersonalHistoryListViewAdapter(PersonalHistoryActivity.this,listVal);
                        // listView.setAdapter(pAdapter);
                        // listVal.addAll(data1.getList());
                        pAdapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_personal_history, container, false);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        editSign = 0;
        allSign = 0;
        delSign = 0;

        requestHisData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void initView() {
        listView = (ListView)view.findViewById(R.id.personal_history_listview);
        listView.setOnItemClickListener(this);
        titleLeft = (TextView) view.findViewById(R.id.common_title_left);
        titleCenter = (TextView) view.findViewById(R.id.common_title_center);
        titleRight = (TextView) view.findViewById(R.id.common_title_right);

        allView = (TextView) view.findViewById(R.id.personal_hy_all);
        deleteView = (TextView) view.findViewById(R.id.personal_hy_delete);
        deleteView.setTextColor(Color.GRAY);
        historyLayout = (LinearLayout) view
                .findViewById(R.id.personal_history_item_detail_bottom);
        dataLayout = (RelativeLayout) view
                .findViewById(R.id.personal_history_data_layout);
        netImg = (ImageView) view.findViewById(R.id.personal_hy_net_layout);

        titleCenter.setText(getString(R.string.history_page));
        titleRight.setText(getString(R.string.edit));

        listVal = new ArrayList<PlayHistoryEntity>();


        pAdapter = new PersonalHistoryListViewAdapter(
                getActivity(), listVal);
        listView.setAdapter(pAdapter);

        titleLeft.setOnClickListener(new ViewClick());
        allView.setOnClickListener(new ViewClick());
        deleteView.setOnClickListener(new ViewClick());
        titleRight.setOnClickListener(new ViewClick());
    }

    class PIHyData {
        private List<PlayHistoryEntity> list;

        public List<PlayHistoryEntity> getList() {
            return list;
        }

        public void setList(List<PlayHistoryEntity> list) {
            this.list = list;
        }

        @Override
        public String toString() {
            return "PIHyData [list=" + list + "]";
        }

    }

    private void requestHisData() {
        showLoadingDialog();
        final ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!getActivity().isFinishing()) {
                    try {
                        List<PlayHistoryEntity> list = dbInterface.getPlayHistoryOrderByPlayTime();
                        if (null != list) {
                            listVal.addAll(list);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "数据查询出错！！", Toast.LENGTH_SHORT).show();
                    } finally {
                        {

                           getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    dismissLoadDialog();
                                    es.shutdownNow();
                                    es.shutdown();
                                    if (listVal.size() == 0) {
                                        // showToast("暂无观看历史！");
                                        titleRight.setVisibility(View.GONE);
                                        return;
                                    }
                                    titleRight.setVisibility(View.VISIBLE);
                                    dataLayout.setVisibility(View.VISIBLE);
                                    netImg.setVisibility(View.GONE);
                                    pAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }
            }
        });

    }

    class ViewClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.common_title_right:

                    if (editSign == 0) {
                        editSign = 1;
                        titleRight.setText(getString(R.string.cancel));
                        historyLayout.setVisibility(View.VISIBLE);

                        for (PlayHistoryEntity history : listVal) {
                            history.setSign(1);
                            history.setIscheck(false);
                        }
                        pAdapter.notifyDataSetChanged();

                    } else if(editSign== 1){
                        titleRight.setText(getString(R.string.edit));
                        editSign = 0;
                        historyLayout.setVisibility(View.GONE);

                        for (PlayHistoryEntity history : listVal) {
                            history.setSign(0);
                        }
                        pAdapter.notifyDataSetChanged();
                        deleteView.setText(getString(R.string.delete));
                        deleteView.setTextColor(Color.GRAY);
                        allView.setText(getString(R.string.select_all));
                        allView.setTag(null);
                    }else{
                        titleRight.setText(getString(R.string.edit));
                        editSign = 0;
                        historyLayout.setVisibility(View.GONE);

                        for (PlayHistoryEntity history : listVal) {
                            history.setSign(0);
                        }
                        pAdapter.notifyDataSetChanged();
                        deleteView.setText(getString(R.string.delete));
                        deleteView.setTextColor(Color.GRAY);
                        allView.setText(getString(R.string.select_all));
                        allView.setTag(null);

                    }
                    break;
                case R.id.personal_hy_delete:
                    final List<PlayHistoryEntity> list = new ArrayList<PlayHistoryEntity>();
                    for (PlayHistoryEntity phm : listVal) {
                        if (phm.getIscheck()) {
                            list.add(phm);
                        }
                    }
                    if (list.size() == 0)
                        return;
                    else

                        // final Iterator<PlayHistoryModel> it = listVal.iterator();
                        // while(it.hasNext()){
                        // PlayHistoryModel pmodel = it.next();
                        // if(pmodel.isIscheck()){
                        // try {
                        // DBHelper.getDbManager().delete(pmodel);
                        // } catch (DbException e) {
                        // // TODO Auto-generated catch block
                        // e.printStackTrace();
                        // }
                        // it.remove();
                        // }
                        // }
                        showDialog(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                try {
                                    dbInterface.deletePlayHistory(list);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                listVal.removeAll(list);
                                deleteView.setText(getString(R.string.delete));
                                deleteView.setTextColor(Color.GRAY);
                                allView.setText(getString(R.string.select_all));
                                allView.setTag(null);
                                pAdapter.notifyDataSetChanged();
                                if (listVal.size() == 0) {
                                    historyLayout.setVisibility(View.GONE);
                                    netImg.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    break;
                case R.id.personal_hy_all:
                    for (PlayHistoryEntity pm : listVal) {
                        pm.setIscheck(view.getTag() == null);
                    }
                    if (null == allView.getTag()) {
                        (allView).setText(getString(R.string.cancel_select_all));
                        allView.setTag(view);
                        deleteView.setText(getString(R.string.delete) + listVal.size());
                        deleteView.setTextColor(Color.parseColor("#1f539e"));
                    } else {
                        (allView).setText(getString(R.string.select_all));
                        allView.setTag(null);
                        deleteView.setText(getString(R.string.delete));
                        deleteView.setTextColor(Color.parseColor("#1f539e"));
                    }
                    pAdapter.notifyDataSetChanged();
                    break;
                case R.id.common_title_left:
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PersonalActivity.class);
                    startActivity(intent);
                    break;
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View arg1, int position,
                            long id) {
        PlayHistoryEntity historyEntity = (PlayHistoryEntity) parent.getAdapter()
                .getItem(position);

        Log.e("his", historyEntity.toString());

//		PlayVodEntity pve = new PlayVodEntity(
//				CollectTypeEnum.SP.value() + "",
//				model.getPid(),
//				model.getVid(),
//				model.getPageurl(),
//				model.getVideoImg(),
//				model.getTitle(),
//				"",
//				Integer.parseInt(StringUtil.isNullOrEmpty(model.getVideoType()) ? "-1"
//						: model.getVideoType()), model.getTimeLenth());

        //全部当成单视频处理
        PlayVodEntity pve = new PlayVodEntity(
                CollectTypeEnum.SP.value() + "",
                historyEntity.getPid(),
                null,
                historyEntity.getPageurl(),
                historyEntity.getVideoImg(),
                historyEntity.getTitle(),
                "",
                Integer.parseInt(StringUtil.isNullOrEmpty(historyEntity.getVideoType()) ? "-1"
                        : historyEntity.getVideoType()), historyEntity.getTimeLenth());
        if (!historyLayout.isShown()) {
            // showToast(model.toString()+"==="+position);

            Intent it = new Intent();
            it.setClass(getActivity(), PlayVodFullScreeActivity.class);
            it.putExtra("vid", pve);
            startActivity(it);
        } else {
            historyEntity.setIscheck(!historyEntity.getIscheck());
            int size = 0;
            for (PlayHistoryEntity ph : listVal) {
                if (ph.getIscheck())
                    size++;
            }
            if (size == listVal.size()) {
                (allView).setText(getString(R.string.cancel_select_all));
                allView.setTag(allView);
            } else {
                (allView).setText(getString(R.string.select_all));
                allView.setTag(null);
            }
            deleteView.setText(size == 0 ? getString(R.string.delete) : getString(R.string.delete) + size);
            deleteView.setTextColor(Color.parseColor("#1f539e"));
            pAdapter.notifyDataSetChanged();
        }
    }

    public void showDialog(final View.OnClickListener click) {
        View viewDialog = (View) LayoutInflater.from(getActivity()).inflate(
                R.layout.delete_dialog, null);
        final Dialog dialog = new Dialog(getActivity(),
                R.style.loginDialogTheme);
        dialog.setCancelable(false);
        TextView dialogCancel, dialogRightsure, dialog_title, dialog_content;
        dialogRightsure = (TextView) viewDialog
                .findViewById(R.id.login_right_sure);
        dialogCancel = (TextView) viewDialog
                .findViewById(R.id.login_cancel_but);
        dialog_title = (TextView) viewDialog.findViewById(R.id.del_title_tv);
        dialog_content = (TextView) viewDialog
                .findViewById(R.id.del_content_tv);
        dialog_content.setVisibility(View.VISIBLE);
        dialog_title.setText(getString(R.string.tips));
        dialog_content.setText(getString(R.string.del_tip));
        dialog.setContentView(viewDialog);
        dialogCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialogRightsure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                click.onClick(arg0);
                titleRight.setVisibility(View.VISIBLE);
                titleRight.setText(getString(R.string.finish));
                editSign = 2;
                if (listVal.size() == 0) {
                    // showToast("暂无观看历史！");
                    titleRight.setVisibility(View.GONE);
                }
                dialog.dismiss();

                //titleRight.setVisibility(View.GONE);
            }
        });
        dialog.show();
    }
}
