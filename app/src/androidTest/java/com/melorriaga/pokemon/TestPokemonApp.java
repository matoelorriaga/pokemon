package com.melorriaga.pokemon;

import android.support.annotation.NonNull;

import com.melorriaga.pokemon.injection.component.AppComponent;
import com.melorriaga.pokemon.injection.component.DaggerAppComponent;
import com.melorriaga.pokemon.injection.module.AppModule;
import com.melorriaga.pokemon.injection.module.NetworkModule;

import io.appflate.restmock.RESTMockServer;

/**
 * Created by melorriaga on 4/2/17.
 */
public class TestPokemonApp extends PokemonApp {

    @NonNull
    @Override
    public AppComponent getAppComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(RESTMockServer.getUrl()))
                .build();
    }

}
