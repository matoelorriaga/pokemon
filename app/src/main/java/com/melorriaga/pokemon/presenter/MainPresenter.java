package com.melorriaga.pokemon.presenter;

import com.melorriaga.pokemon.view.MainView;

public interface MainPresenter extends BasePresenter<MainView> {

    void getPokemonNames();

}