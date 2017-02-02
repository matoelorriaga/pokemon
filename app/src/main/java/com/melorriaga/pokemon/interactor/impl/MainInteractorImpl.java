package com.melorriaga.pokemon.interactor.impl;

import com.melorriaga.pokemon.interactor.MainInteractor;
import com.melorriaga.pokemon.model.APIResourceList;
import com.melorriaga.pokemon.model.NamedAPIResource;
import com.melorriaga.pokemon.model.api.PokemonAPI;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public final class MainInteractorImpl implements MainInteractor {

    private static final int POKEMON_AMOUNT = 150;

    private PokemonAPI mPokemonAPI;
    private Subscription mSubscription;

    private boolean mNetworkRequestInProgress = false;

    public MainInteractorImpl(PokemonAPI pokemonAPI) {
        mPokemonAPI = pokemonAPI;
    }

    @Override
    public void getPokemonNames(final OnGetPokemonNamesListener listener) {
        mNetworkRequestInProgress = true;
        mSubscription = mPokemonAPI.getPokemonNames(POKEMON_AMOUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<APIResourceList, List<String>>() {
                    @Override
                    public List<String> call(APIResourceList apiResourceList) {
                        List<NamedAPIResource> results = apiResourceList.results;
                        List<String> pokemonNames = new ArrayList<>();
                        for (NamedAPIResource result : results) {
                            pokemonNames.add(result.name);
                        }
                        return pokemonNames;
                    }
                })
                .subscribe(new Observer<List<String>>() {

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
                    public void onNext(List<String> pokemonNames) {
                        mNetworkRequestInProgress = false;
                        listener.onSuccess(pokemonNames);
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
