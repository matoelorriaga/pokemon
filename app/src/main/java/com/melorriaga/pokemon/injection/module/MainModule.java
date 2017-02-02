package com.melorriaga.pokemon.injection.module;

import android.support.annotation.NonNull;

import com.melorriaga.pokemon.injection.scope.ActivityScope;
import com.melorriaga.pokemon.interactor.MainInteractor;
import com.melorriaga.pokemon.interactor.impl.MainInteractorImpl;
import com.melorriaga.pokemon.model.api.PokemonAPI;
import com.melorriaga.pokemon.presenter.loader.PresenterFactory;
import com.melorriaga.pokemon.presenter.MainPresenter;
import com.melorriaga.pokemon.presenter.impl.MainPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class MainModule {

    @Provides
    @ActivityScope
    public MainInteractor provideInteractor(PokemonAPI pokemonAPI) {
        return new MainInteractorImpl(pokemonAPI);
    }

    @Provides
    @ActivityScope
    public PresenterFactory<MainPresenter> providePresenterFactory(@NonNull final MainInteractor interactor) {
        return new PresenterFactory<MainPresenter>() {
            @NonNull
            @Override
            public MainPresenter create() {
                return new MainPresenterImpl(interactor);
            }
        };
    }

}
