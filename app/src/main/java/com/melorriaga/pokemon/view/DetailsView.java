package com.melorriaga.pokemon.view;

import android.support.annotation.UiThread;

import com.melorriaga.pokemon.model.Pokemon;

@UiThread
public interface DetailsView {

    void showLoadingIndicator();

    void hideLoadingIndicator();

    int getPokemonId();

    void showPokemonDetails(Pokemon pokemon);

    void showDoneMessage();

    void showErrorMessage();

}