package com.melorriaga.pokemon.injection.module;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.melorriaga.pokemon.PokemonApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {

    @NonNull
    private final PokemonApp mPokemonApp;

    public AppModule(@NonNull PokemonApp pokemonApp) {
        mPokemonApp = pokemonApp;
    }

    @Provides
    @Singleton
    public PokemonApp providePokemonApp() {
        return mPokemonApp;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mPokemonApp);
    }

    @Provides
    @Singleton
    public Resources provideResources() {
        return mPokemonApp.getResources();
    }

}
