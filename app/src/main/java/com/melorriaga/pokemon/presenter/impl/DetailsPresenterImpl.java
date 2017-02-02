package com.melorriaga.pokemon.presenter.impl;

import android.support.annotation.NonNull;

import com.melorriaga.pokemon.interactor.DetailsInteractor;
import com.melorriaga.pokemon.model.Pokemon;
import com.melorriaga.pokemon.presenter.DetailsPresenter;
import com.melorriaga.pokemon.view.DetailsView;

public final class DetailsPresenterImpl extends BasePresenterImpl<DetailsView> implements DetailsPresenter, DetailsInteractor.OnGetPokemonDetailsListener {

    private Pokemon mPokemon;

    /**
     * The interactor
     */
    @NonNull
    private final DetailsInteractor mInteractor;

    // The view is available using the mView variable

    public DetailsPresenterImpl(@NonNull DetailsInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean firstStart) {
        super.onStart(firstStart);

        // Your code here. Your view is available using mView and will not be null until next onStop()

        if (firstStart) {
            int pokemonId = mView.getPokemonId();
            getPokemonDetails(pokemonId);
        } else {
            if (mInteractor.networkRequestInProgress()) {
                mView.showLoadingIndicator();
            }
            mView.showPokemonDetails(mPokemon);
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
    public void getPokemonDetails(int id) {
        mView.showLoadingIndicator();
        mInteractor.getPokemonDetails(id, this);
    }

    @Override
    public void onSuccess(Pokemon pokemon) {
        mPokemon = pokemon;

        mView.showPokemonDetails(pokemon);
        mView.hideLoadingIndicator();
        mView.showDoneMessage();
    }

    @Override
    public void onFailure() {
        mView.hideLoadingIndicator();
        mView.showErrorMessage();
    }

}