package com.melorriaga.pokemon.interactor;

import java.util.List;

public interface MainInteractor extends BaseInteractor {

    interface OnGetPokemonNamesListener {
        void onSuccess(List<String> pokemonNames);
        void onFailure();
    }

    void getPokemonNames(OnGetPokemonNamesListener listener);

    boolean networkRequestInProgress();

    void cancelNetworkRequest();

}
