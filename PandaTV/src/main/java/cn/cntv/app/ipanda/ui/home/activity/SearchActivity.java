package cn.cntv.app.ipanda.ui.home.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.api.PandaApi;
import cn.cntv.app.ipanda.data.net.retrofit.Call;
import cn.cntv.app.ipanda.data.net.retrofit.Callback;
import cn.cntv.app.ipanda.data.net.retrofit.Response;
import cn.cntv.app.ipanda.ui.home.adapter.AutoSearchAdapter;
import cn.cntv.app.ipanda.ui.home.adapter.HistoryWordsAdapter;
import cn.cntv.app.ipanda.ui.home.adapter.SearchResultAdapter;
import cn.cntv.app.ipanda.ui.home.entity.AutoSearchListData;
import cn.cntv.app.ipanda.ui.home.entity.HITSBean;
import cn.cntv.app.ipanda.utils.SharePreferenceUtil;
import cn.cntv.app.ipanda.xlistview.XListView;


public class SearchActivity extends Activity implements View.OnClickListener, TextWatcher {

    private TextView mSearchBtn, mHistoryDelete, no_result;
    private EditText mSearchEdit;
    private ImageView mClearBtn;

    private LinearLayout  mHistoryLayout,mHotLayout,mResultLayout;
    private RelativeLayout mSearchLayout;

    private int count;
    private String[] strings;

    private ListView mAutoList;//搜索下拉框
    private GridView mHistWordsList;//历史搜索列表
    private GridView mHotWordsList;//热门搜索列表
    private XListView mResultList;//搜索结果展示

    private List<HITSBean> mAutoListData = new ArrayList<HITSBean>();//搜索下拉框数据


    private SharePreferenceUtil mSp;
    private PopupWindow mPopw;
    private HashSet<String> has;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search);

        initView();
    }

    private void initView() {

        mSp = new SharePreferenceUtil(this);
        int counts = mSp.getCounts();
        this.count = counts;

        mClearBtn = (ImageView) this.findViewById(R.id.iv_clear_searchedit);
        TextView TitleLeft = (TextView) this.findViewById(R.id.common_title_left);
        //关键字输入框
        mSearchEdit = (EditText) this.findViewById(R.id.et_search);
        //搜索按钮
        mSearchBtn = (TextView) this.findViewById(R.id.tv_search_btn);
        //历史搜索删除按钮
        mHistoryDelete = (TextView) this.findViewById(R.id.tv_history_delete);
        //搜索区域布局（输入框）
        mSearchLayout = (RelativeLayout) this.findViewById(R.id.layout_search);
        //历史搜索布局
        mHistoryLayout = (LinearLayout) this.findViewById(R.id.layout_history_words);
        //热门搜索布局
        mHotLayout = (LinearLayout) this.findViewById(R.id.layout_hot_words);
        //输入框下拉列表
        mAutoList = (ListView) this.findViewById(R.id.listv_autosearch);
        //历史搜索
        mHistWordsList = (GridView) this.findViewById(R.id.gv_history);
        //热门搜索
        mHotWordsList = (GridView) this.findViewById(R.id.gv_hot);
        //搜索结果展示列表
        mResultList = (XListView) this.findViewById(R.id.search_result_listview);
        //搜索结果布局
        mResultLayout = (LinearLayout) this.findViewById(R.id.search_result_layout);

        mResultList.setPullLoadEnable(false);
        mSearchEdit.addTextChangedListener(this);
        TitleLeft.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);
        mHistoryDelete.setOnClickListener(this);
        mClearBtn.setOnClickListener(this);
        itemClick();

        mSearchBtn.setVisibility(View.VISIBLE);
        mSearchLayout.setVisibility(View.VISIBLE);

        getHistoryWords();

        if ( strings == null ) {
            //没有数据隐藏历史搜索
            mHistoryLayout.setVisibility(View.GONE);
        } else {
            mHistoryLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                if(mPopw != null &&mPopw.isShowing()){
                    mPopw.dismiss();
                }
                break;

            case R.id.common_title_left:

                finish();
                break;
            case R.id.iv_clear_searchedit:
                mSearchEdit.setText(null);

                break;
            case R.id.tv_search_btn:
                hideKeyBoard();
                String histwords = mSearchEdit.getText().toString().trim();
                if("".equals(histwords)||histwords == null){
                    Toast.makeText(this, R.string.can_not_be_empty, Toast.LENGTH_SHORT).show();
                        return ;
                }
                flag =102;
                setHistoryWords(histwords);
                requestAutoSearch(histwords);
                mAutoList.setVisibility(View.GONE);
                mHistoryLayout.setVisibility(View.GONE);
                mHotLayout.setVisibility(View.GONE);

                mResultList.setAdapter(new SearchResultAdapter(SearchActivity.this,mAutoListData));
                mResultLayout.setVisibility(View.VISIBLE);

                break;
            case R.id.tv_history_delete:
                mHistoryLayout.setVisibility(View.GONE);
                strings = null;
                for (int i = 1; i <= count; i++) {
                    mSp.removeAll(i);
                }
                count =0;
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence cha, int i, int i1, int i2) {
        if(flag == 101){
            flag = 100;
            return;
        }
        if(cha.length() > 0){
            mClearBtn.setVisibility(View.VISIBLE);
        }else{
            mClearBtn.setVisibility(View.GONE);
        }
        try {

            String qText = URLEncoder.encode(cha.toString(), "UTF-8");
            if (!qText.isEmpty()) {

                requestAutoSearch(qText);
            } else {
                mAutoList.setVisibility(View.GONE);
                no_result.setVisibility(View.GONE);
                mResultLayout.setVisibility(View.GONE);
                mHotLayout.setVisibility(View.VISIBLE);
                if(mAutoListData.size() != 0)
                mHistoryLayout.setVisibility(View.VISIBLE);
                mAutoListData.clear();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    private int flag ;
    private void itemClick() {

        mAutoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mHistoryLayout.setVisibility(View.GONE);
                mHotLayout.setVisibility(View.GONE);
                TextView tv = (TextView) view.findViewById(R.id.tv_auto_search_title);
                String trim = tv.getText().toString().trim();
                flag =101;
                mSearchEdit.setText(tv.getText());

                mSearchEdit.setSelection(trim.length());
                mAutoList.setVisibility(View.GONE);
                setHistoryWords(trim);
                mResultList.setAdapter(new SearchResultAdapter(SearchActivity.this,mAutoListData));
                mResultLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 自动获取关键字的数据
     *
     * @param qtext
     */
    private void requestAutoSearch(String qtext) {
        PandaApi.itemClickSearch(qtext, 10,1).enqueue(new Callback<AutoSearchListData>() {
            @Override
            public void onResponse(Call<AutoSearchListData> call, Response<AutoSearchListData> response) {
                AutoSearchListData autoSearch = response.body();
                List<HITSBean> hits = autoSearch.getHITS();
                Log.i("ERROR","error==="+autoSearch.getMESSAGE());
                if (autoSearch.getMESSAGE().equals("SUCCESS")) {
                    mAutoListData.clear();
                    mAutoListData.addAll(hits);
                    if (mAutoListData != null && flag != 102 ) {
                        AutoSearchAdapter autoSearchAdapter = new AutoSearchAdapter(SearchActivity.this, mAutoListData);
                        mAutoList.setAdapter(autoSearchAdapter);
                        //autoSearchAdapter.notifyDataSetChanged();
                        mAutoList.setVisibility(View.VISIBLE);

                    }

                }

                if(autoSearch.getMESSAGE().equals("no_result")&& flag == 102){
                    flag = 100;
                    no_result = (TextView) SearchActivity.this.findViewById(R.id.tv_no_result);
                    no_result.setVisibility(View.VISIBLE);
                    mResultLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AutoSearchListData> call, Throwable t) {

            }

        });
    }



    private void requestSearchBtn(String qtext){

    }

    /**
     * 保存搜索关键词记录
     */
    private void setHistoryWords(String qtext) {
        hideKeyBoard();
        if (!"".equals(qtext) && qtext != null) {
            count++;
            mSp.setCounts(count);
            mSp.setHistWords(qtext, count);
            Log.i("HIS","存的时候的count:"+count+"存的时候的内容："+qtext);
            Toast.makeText(this, R.string.already_saved_to_search_history, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 提取历史搜索关键词
     */
    private void getHistoryWords() {

        int counts = mSp.getCounts();
        HashSet<String> has =  new HashSet<String>();

        if (counts != 0) {
            for (int i = 1; i <= counts; i++) {
                String histWords = mSp.getHistWords(i);
                if (!histWords.isEmpty()) {
                    has.add(histWords);
                    Log.i("HIS","取出的时候的count:"+i+"取出的时候的内容："+histWords);
                }
            }
            strings = has.toArray(new String[0]);
            if (strings.length > 0 && strings != null) {
                mHistWordsList.setVisibility(View.VISIBLE);
                mHistWordsList.setAdapter(new HistoryWordsAdapter(this, strings));

            }

        }else{
            strings = null;
        }
        //热门搜索假数据
        String[] hotWords = {"小熊猫", "大熊猫", "老熊猫", "好熊猫", "坏熊猫", "萌萌哒熊猫", "胖熊猫"};
        mHotWordsList.setAdapter(new HistoryWordsAdapter(this, hotWords));

    }

    /**
     * 隐藏键盘
     */
    private void hideKeyBoard() {

        View tView = getWindow().peekDecorView();

        if (tView != null) {
            // 获取输入法接口
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            // 强制隐藏键盘
            imm.hideSoftInputFromWindow(tView.getWindowToken(), 0);
        }
    }

}
