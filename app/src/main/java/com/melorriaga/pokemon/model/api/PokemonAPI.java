package com.melorriaga.pokemon.model.api;

import com.melorriaga.pokemon.model.APIResourceList;
import com.melorriaga.pokemon.model.Pokemon;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by melorriaga on 2/2/17.
 */
public interface PokemonAPI {

    @GET("pokemon")
    Observable<APIResourceList> getPokemonNames(@Query("limit") Integer limit);

    @GET("pokemon/{id}")
    Observable<Pokemon> getPokemonDetails(@Path("id") Integer id);

}
