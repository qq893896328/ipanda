package cn.cntv.app.ipanda.ui.pandaeye.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cntv.app.ipanda.R;
import cn.cntv.app.ipanda.di.components.FavoriteComponent;
import cn.cntv.app.ipanda.domain.model.Favorite;
import cn.cntv.app.ipanda.presenter.PandEyeDetailPresenter;
import cn.cntv.app.ipanda.ui.BaseFragment;
import cn.cntv.app.ipanda.ui.pandaeye.activity.PandaEyeDetailActivity;
import cn.cntv.app.ipanda.ui.pandaeye.entity.PEArticle;
import cn.cntv.app.ipanda.ui.play.ShareController;
import cn.cntv.app.ipanda.ui.view.PandaEyeDetailView;
import cn.cntv.app.ipanda.utils.DensityUtil;
import cn.cntv.app.ipanda.utils.NetUtil;
import cn.cntv.app.ipanda.utils.PopWindowUtils;
import cn.cntv.app.ipanda.utils.ScreenUtils;

/**
 * Created by yuerq on 16/7/4.
 */
public class PandaEyeDetailFragment extends BaseFragment implements PandaEyeDetailView {

    @BindView(R.id.pe_listview_item_detail_webview)
    WebView webView;

    @BindView(R.id.common_title_left)
    TextView titleLeft;

    @BindView(R.id.pe_listview_item_shareview)
    TextView shareView;

    @BindView(R.id.collect)
    ImageView ivCollect;

    @BindView(R.id.article_no_net_layout)
    RelativeLayout ivNoNet;

    private Favorite favorite;

    private int type;


    @Inject
    PandEyeDetailPresenter pandEyeDetailPresenter;
    private ShareController shareController = new ShareController();


    public static PandaEyeDetailFragment newInstance(Favorite favorite, int type) {
        PandaEyeDetailFragment fragment = new PandaEyeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("favorite", favorite);
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(FavoriteComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.activity_pandaeye_detail, container, false);
        ButterKnife.bind(this, fragmentView);

        initData();

        initializeWebView();

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pandEyeDetailPresenter.setView(this);

        if (type == PandaEyeDetailActivity.TYPE_ARTICLE) {
            pandEyeDetailPresenter.loadArticle(favorite.getId());
        } else {
            if (NetUtil.isNetConnected(getActivity())) {
                webView.loadUrl(favorite.getUrl());
            }
        }


        pandEyeDetailPresenter.retriveFavoriteState(favorite.getId());

    }

    private void initData() {

        Bundle args = getArguments();
        favorite = (Favorite) args.getSerializable("favorite");
        type = args.getInt("type");
    }

    @OnClick(R.id.common_title_left)
    void titleLeftClick() {
        getActivity().finish();
    }

    @OnClick(R.id.pe_listview_item_shareview)
    void shareClick(View v) {
        if (type == PandaEyeDetailActivity.TYPE_ARTICLE) {
            shareController.showPopWindow(v, favorite.getTitle(), favorite.getId(), "6", null, favorite.getPath(), favorite.getId());
        } else {
            shareController.showPopWindow(v, favorite.getTitle(),"4",favorite.getPath(), favorite.getUrl());
        }
    }

    @OnClick(R.id.collect)
    void collectClick() {

        pandEyeDetailPresenter.doFavorite(favorite);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void initializeWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (type != PandaEyeDetailActivity.TYPE_ARTICLE) {
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setBuiltInZoomControls(false);
            webSettings.setSupportZoom(false);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setSupportZoom(true);  //支持缩放
            webSettings.setAllowFileAccess(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setLoadsImagesAutomatically(true);
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 16) {
                webSettings.setMediaPlaybackRequiresUserGesture(false);
            }
            String ua = webSettings.getUserAgentString();
            webSettings.setUserAgentString(ua + ";cntv_app_client_pandaandroid_mobile");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
        pandEyeDetailPresenter.destroy();
    }

    @Override
    public void showFavoriteIcon(boolean favorited) {
        ivCollect.setImageResource(favorited ? R.drawable.collect_yes : R.drawable.collect_no);
    }

    @Override
    public void favoriteSuccess() {
        PopWindowUtils.getInstance().showPopWindowCenter(getActivity(),
                ivCollect, R.layout.ppw_define_cue_center,
                this.getResources().getString(R.string.collect_yes),
                true, 2000);
    }

    @Override
    public void favoriteFailed() {
        PopWindowUtils.getInstance().showPopWindowCenter(getActivity(),
                ivCollect, R.layout.ppw_define_cue_center,
                this.getResources().getString(R.string.collect_no),
                true, 2000);
    }

    @Override
    public void renderArticle(PEArticle article) {

        String title = article.getTitle();
        title = "<strong><font size=4px color='#000000'>"
                + title + "</font></strong>";
        String secTitle = "<p><font size=2px color='#656565'>"
                + article.getSource() + "&nbsp;&nbsp;&nbsp;"
                + article.getPubtime() + "</font></p>";

        String content = title + secTitle
                + article.getContent();

        content = changeImage(content);
        
        webView.loadData(content,  "text/html; charset=UTF-8",
                null);
    }

    // 将图片修改为匹配手机
    private String changeImage(String string) {
        if (null == string) {
            return "";
        } else {
            return string.replaceAll(
                    "<img ",
                    "<img width=\""
                            + (DensityUtil.px2dip(this.getActivity(),
                            ScreenUtils.getScreenSize(this.getActivity(), true)[0])
                            * 4 / 5) + "\"");
        }

    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadDialog();
    }

}
