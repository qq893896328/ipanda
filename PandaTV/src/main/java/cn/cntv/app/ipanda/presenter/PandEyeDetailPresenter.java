package cn.cntv.app.ipanda.presenter;

import javax.inject.Inject;

import cn.cntv.app.ipanda.domain.model.Favorite;
import cn.cntv.app.ipanda.domain.source.PandaRepository;
import cn.cntv.app.ipanda.manager.TransformerManager;
import cn.cntv.app.ipanda.ui.pandaeye.entity.PEArticle;
import cn.cntv.app.ipanda.ui.view.PandaEyeDetailView;
import rx.Subscriber;

/**
 * Created by yuerq on 16/7/4.
 */
public class PandEyeDetailPresenter implements Presenter<PandaEyeDetailView> {

    private PandaEyeDetailView pandaEyeDetailView;


    @Inject
    PandaRepository repository;

    @Inject
    TransformerManager transformerManager;

    @Inject
    public PandEyeDetailPresenter() {
    }

    @Override
    public void setView(PandaEyeDetailView view) {
        this.pandaEyeDetailView = view;
    }


    public void retriveFavoriteState(String favId) {
        boolean favorited = repository.isFavorited(favId);
        pandaEyeDetailView.showFavoriteIcon(favorited);

    }
    public void doFavorite(Favorite favorite) {

        boolean favorited = repository.isFavorited(favorite.getId());

        boolean newState = !favorited;

        repository.addOrRemoveFavorite(favorite, newState);

        pandaEyeDetailView.showFavoriteIcon(newState);
        if (newState) {
            pandaEyeDetailView.favoriteSuccess();
        } else {
            pandaEyeDetailView.favoriteFailed();
        }

    }

    public void loadArticle(String id) {

        repository.getArticle(id)
                .compose(transformerManager.<PEArticle>applySchedulers())
                .subscribe(new ArticleSubscribe());

    }

    private final class ArticleSubscribe extends Subscriber<PEArticle> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onNext(PEArticle article) {
            pandaEyeDetailView.renderArticle(article);

        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
    }

}
