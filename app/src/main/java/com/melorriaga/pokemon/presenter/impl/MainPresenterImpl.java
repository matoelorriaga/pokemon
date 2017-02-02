package com.melorriaga.pokemon.presenter.impl;

import android.support.annotation.NonNull;

import com.melorriaga.pokemon.interactor.MainInteractor;
import com.melorriaga.pokemon.presenter.MainPresenter;
import com.melorriaga.pokemon.view.MainView;

import java.util.List;

public final class MainPresenterImpl extends BasePresenterImpl<MainView> implements MainPresenter, MainInteractor.OnGetPokemonNamesListener {

    private List<String> mPokemonNames;

    /**
     * The interactor
     */
    @NonNull
    private final MainInteractor mInteractor;

    // The view is available using the mView variable

    public MainPresenterImpl(@NonNull MainInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean firstStart) {
        super.onStart(firstStart);

        // Your code here. Your view is available using mView and will not be null until next onStop()

        if (firstStart) {
            getPokemonNames();
        } else {
            if (mInteractor.networkRequestInProgress()) {
                mView.showLoadingIndicator();
            }
            mView.showPokemonNames(mPokemonNames);
        }
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()

        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */

        mInteractor.cancelNetworkRequest();

        super.onPresenterDestroyed();
    }

    @Override
    public void getPokemonNames() {
        mView.showLoadingIndicator();
        mInteractor.getPokemonNames(this);
    }

    @Override
    public void onSuccess(List<String> pokemonNames) {
        mPokemonNames = pokemonNames;

        mView.showPokemonNames(pokemonNames);
        mView.hideLoadingIndicator();
        mView.showDoneMessage();
    }

    @Override
    public void onFailure() {
        mView.hideLoadingIndicator();
        mView.showErrorMessage();
    }
    
}