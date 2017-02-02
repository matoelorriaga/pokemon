package com.melorriaga.pokemon.view;

import android.support.annotation.UiThread;

import java.util.List;

@UiThread
public interface MainView {

    void showLoadingIndicator();

    void hideLoadingIndicator();

    void showPokemonNames(List<String> pokemonNames);

    void showDoneMessage();

    void showErrorMessage();

}
