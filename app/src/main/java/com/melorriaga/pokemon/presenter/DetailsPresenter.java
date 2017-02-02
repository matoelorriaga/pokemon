package com.melorriaga.pokemon.presenter;

import com.melorriaga.pokemon.view.DetailsView;

public interface DetailsPresenter extends BasePresenter<DetailsView> {

    void getPokemonDetails(int id);

}