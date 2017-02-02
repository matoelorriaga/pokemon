package com.melorriaga.pokemon.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.melorriaga.pokemon.R;
import com.melorriaga.pokemon.injection.component.AppComponent;
import com.melorriaga.pokemon.injection.component.DaggerMainComponent;
import com.melorriaga.pokemon.injection.module.MainModule;
import com.melorriaga.pokemon.presenter.MainPresenter;
import com.melorriaga.pokemon.presenter.loader.PresenterFactory;
import com.melorriaga.pokemon.view.MainView;
import com.melorriaga.pokemon.view.adapter.recyclerview.PokemonRecyclerViewAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class MainActivity extends BaseActivity<MainPresenter, MainView> implements MainView {

    @Inject
    PresenterFactory<MainPresenter> mPresenterFactory;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private PokemonRecyclerViewAdapter mPokemonRecyclerViewAdapter;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerMainComponent.builder()
                .appComponent(parentComponent)
                .mainModule(new MainModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<MainPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.

        initToolbar();
        initSwipeRefreshLayout();
        initRecyclerView();
    }

    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getPokemonNames();
            }
        });
    }

    private void initRecyclerView() {
        mPokemonRecyclerViewAdapter = new PokemonRecyclerViewAdapter();
        mPokemonRecyclerViewAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mRecyclerView.getChildAdapterPosition(v);
                String pokemonName = mPokemonRecyclerViewAdapter.getPokemonNames().get(position);

                Intent intent = Henson.with(MainActivity.this)
                        .gotoDetailsActivity()
                        .pokemonId(position + 1)
                        .pokemonName(pokemonName)
                        .build();
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mPokemonRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showLoadingIndicator() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoadingIndicator() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showPokemonNames(List<String> pokemonNames) {
        mPokemonRecyclerViewAdapter.setPokemonNames(pokemonNames);
    }

    @Override
    public void showDoneMessage() {
        Snackbar.make(mCoordinatorLayout,R.string.done, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage() {
        Snackbar.make(mCoordinatorLayout,R.string.error, Snackbar.LENGTH_SHORT)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.getPokemonNames();
                    }
                })
                .show();
    }

}
