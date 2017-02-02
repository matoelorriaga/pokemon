package com.melorriaga.pokemon.injection.module;

import android.support.annotation.NonNull;

import com.melorriaga.pokemon.injection.scope.ActivityScope;
import com.melorriaga.pokemon.interactor.DetailsInteractor;
import com.melorriaga.pokemon.interactor.impl.DetailsInteractorImpl;
import com.melorriaga.pokemon.model.api.PokemonAPI;
import com.melorriaga.pokemon.presenter.loader.PresenterFactory;
import com.melorriaga.pokemon.presenter.DetailsPresenter;
import com.melorriaga.pokemon.presenter.impl.DetailsPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class DetailsModule {

    @Provides
    @ActivityScope
    public DetailsInteractor provideInteractor(PokemonAPI pokemonAPI) {
        return new DetailsInteractorImpl(pokemonAPI);
    }

    @Provides
    @ActivityScope
    public PresenterFactory<DetailsPresenter> providePresenterFactory(@NonNull final DetailsInteractor interactor) {
        return new PresenterFactory<DetailsPresenter>() {
            @NonNull
            @Override
            public DetailsPresenter create() {
                return new DetailsPresenterImpl(interactor);
            }
        };
    }

}
