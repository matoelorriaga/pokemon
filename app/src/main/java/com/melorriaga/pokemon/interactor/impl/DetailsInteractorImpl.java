package com.melorriaga.pokemon.interactor.impl;

import com.melorriaga.pokemon.interactor.DetailsInteractor;
import com.melorriaga.pokemon.model.Pokemon;
import com.melorriaga.pokemon.model.api.PokemonAPI;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public final class DetailsInteractorImpl implements DetailsInteractor {

    private PokemonAPI mPokemonAPI;
    private Subscription mSubscription;

    private boolean mNetworkRequestInProgress = false;

    public DetailsInteractorImpl(PokemonAPI pokemonAPI) {
        mPokemonAPI = pokemonAPI;
    }

    @Override
    public void getPokemonDetails(int id, final OnGetPokemonDetailsListener listener) {
        mNetworkRequestInProgress = true;
        mSubscription = mPokemonAPI.getPokemonDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Pokemon>() {

                    @Override
                    public void onCompleted() {
                        // do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mNetworkRequestInProgress = false;
                        listener.onFailure();
                    }

                    @Override
                    public void onNext(Pokemon pokemon) {
                        mNetworkRequestInProgress = false;
                        listener.onSuccess(pokemon);
                    }

                });
    }

    @Override
    public boolean networkRequestInProgress() {
        return mNetworkRequestInProgress;
    }

    @Override
    public void cancelNetworkRequest() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

}