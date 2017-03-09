package com.melorriaga.pokemon

import com.melorriaga.pokemon.injection.component.AppComponent
import com.melorriaga.pokemon.injection.component.DaggerAppComponent
import com.melorriaga.pokemon.injection.module.AppModule
import com.melorriaga.pokemon.injection.module.NetworkModule

import io.appflate.restmock.RESTMockServer

/**
 * Created by melorriaga on 4/2/17.
 */
class TestPokemonApp : PokemonApp() {

    override fun getAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .networkModule(NetworkModule(RESTMockServer.getUrl()))
                .build()
    }

}
