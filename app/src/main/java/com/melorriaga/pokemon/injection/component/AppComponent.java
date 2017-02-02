package com.melorriaga.pokemon.injection.component;

import android.content.res.Resources;

import com.melorriaga.pokemon.injection.module.AppModule;
import com.melorriaga.pokemon.injection.module.NetworkModule;
import com.melorriaga.pokemon.model.api.PokemonAPI;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        NetworkModule.class
})
public interface AppComponent {

    // AppModule
    Resources resources();

    // NetworkModule
    PokemonAPI pokemonAPI();

}
