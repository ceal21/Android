package coreupgrade.reto3final.service;

import java.util.ArrayList;

import coreupgrade.reto3final.entity.PokemonEntity;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Cesar M on 16/02/2016.
 */
public interface ApiService {
    @GET("/lista_pokemons.php")
    void getPokemons(Callback<ArrayList<PokemonEntity>> callback);
}
