package com.melorriaga.pokemon.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.melorriaga.pokemon.PokemonApp;
import com.melorriaga.pokemon.R;
import com.melorriaga.pokemon.injection.component.AppComponent;
import com.melorriaga.pokemon.presenter.BasePresenter;
import com.melorriaga.pokemon.presenter.loader.PresenterFactory;
import com.melorriaga.pokemon.presenter.loader.PresenterLoader;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;

public abstract class BaseActivity<P extends BasePresenter<V>, V> extends AppCompatActivity implements LoaderManager.LoaderCallbacks<P> {

    /**
     * Common counter for views (fragments and activities) that is used to generate loader ids
     */
    static final AtomicInteger sViewCounter = new AtomicInteger(0);

    private final static String RECREATION_SAVED_STATE = "recreation_state";
    private final static String LOADER_ID_SAVED_STATE = "loader_id_state";

    /**
     * Do we need to call {@link #doStart()} from the {@link #onLoadFinished(Loader, BasePresenter)} method.
     * Will be true if presenter wasn't loaded when {@link #onStart()} is reached
     */
    private final AtomicBoolean mNeedToCallStart = new AtomicBoolean(false);

    /**
     * The presenter for this view
     */
    @Nullable
    protected P mPresenter;

    /**
     * Is this the first start of the activity (after onCreate)
     */
    private boolean mFirstStart;

    /**
     * Unique identifier for the loader, persisted across re-creation
     */
    private int mUniqueLoaderIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirstStart = savedInstanceState == null || savedInstanceState.getBoolean(RECREATION_SAVED_STATE);
        mUniqueLoaderIdentifier = savedInstanceState == null ? BaseActivity.sViewCounter.incrementAndGet() : savedInstanceState.getInt(LOADER_ID_SAVED_STATE);

        injectDependencies();

        getSupportLoaderManager().initLoader(mUniqueLoaderIdentifier, null, this).startLoading();
    }

    private void injectDependencies() {
        setupComponent(((PokemonApp) getApplication()).getAppComponent());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mPresenter == null) {
            mNeedToCallStart.set(true);
        } else {
            doStart();
        }
    }

    /**
     * Call the presenter callbacks for onStart
     */
    private void doStart() {
        assert mPresenter != null;

        mPresenter.onViewAttached((V) this);
        mPresenter.onStart(mFirstStart);

        mFirstStart = false;
    }

    @Override
    protected void onStop() {
        if (mPresenter != null) {
            mPresenter.onStop();
            mPresenter.onViewDetached();
        }

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(RECREATION_SAVED_STATE, mFirstStart);
        outState.putInt(LOADER_ID_SAVED_STATE, mUniqueLoaderIdentifier);
    }

    @Override
    public final Loader<P> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, getPresenterFactory());
    }

    @Override
    public final void onLoadFinished(Loader<P> loader, P presenter) {
        mPresenter = presenter;

        if (mNeedToCallStart.compareAndSet(true, false)) {
            doStart();
        }
    }

    @Override
    public final void onLoaderReset(Loader<P> loader) {
        mPresenter = null;
    }

    /**
     * Get the presenter factory implementation for this view
     *
     * @return the presenter factory
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();

    /**
     * Setup the injection component for this view
     *
     * @param appComponent the app component
     */
    protected abstract void setupComponent(@NonNull AppComponent appComponent);

    /*********************************************************************************************/

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    protected void initToolbar() {
        setSupportActionBar(mToolbar);
    }

    protected void setToolbarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                about();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void about() {
        Snackbar.make(mCoordinatorLayout, R.string.about_message, Snackbar.LENGTH_SHORT).show();
    }

}
