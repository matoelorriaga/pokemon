package com.melorriaga.pokemon.interactor;

import com.melorriaga.pokemon.model.Pokemon;

public interface DetailsInteractor extends BaseInteractor {

    interface OnGetPokemonDetailsListener {
        void onSuccess(Pokemon pokemon);
        void onFailure();
    }

    void getPokemonDetails(int id, OnGetPokemonDetailsListener listener);

    boolean networkRequestInProgress();

    void cancelNetworkRequest();

}