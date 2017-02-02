package com.melorriaga.pokemon;

import android.app.Application;
import android.support.annotation.NonNull;

import com.melorriaga.pokemon.injection.component.AppComponent;
import com.melorriaga.pokemon.injection.component.DaggerAppComponent;
import com.melorriaga.pokemon.injection.module.AppModule;
import com.melorriaga.pokemon.injection.module.NetworkModule;

public final class PokemonApp extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(BuildConfig.BASE_URL))
                .build();
    }

    @NonNull
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

}