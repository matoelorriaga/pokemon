package com.melorriaga.pokemon.presenter;

import com.melorriaga.pokemon.interactor.DetailsInteractor;
import com.melorriaga.pokemon.model.Pokemon;
import com.melorriaga.pokemon.model.PokemonSprites;
import com.melorriaga.pokemon.presenter.impl.DetailsPresenterImpl;
import com.melorriaga.pokemon.view.DetailsView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by melorriaga on 3/2/17.
 */
public class DetailsPresenterTest extends BasePresenterTest {

    @Mock
    DetailsView view;

    @Mock
    DetailsInteractor interactor;

    @Captor
    ArgumentCaptor<Integer> integerCaptor;

    @Captor
    ArgumentCaptor<Pokemon> pokemonCaptor;

    @InjectMocks
    DetailsPresenterImpl presenter;

    @Before
    public void setUp() throws Exception {
        Whitebox.setInternalState(presenter, "mView", view);
    }

    @Test
    public void testOnStart_firstStartTrue() {
        presenter.onStart(true);

        verify(view, times(1)).getPokemonId();
        verify(view, times(1)).showLoadingIndicator();
        verify(interactor, times(1)).getPokemonDetails(anyInt(), eq(presenter));
        verifyNoMoreInteractions(view, interactor);
    }

    @Test
    public void testOnStart_firstStartFalse() {
        when(interactor.networkRequestInProgress()).thenReturn(false);

        presenter.onStart(false);

        verify(interactor, times(1)).networkRequestInProgress();
        verify(view, times(1)).showPokemonDetails(any(Pokemon.class));
        verifyNoMoreInteractions(view, interactor);
    }

    @Test
    public void testOnStart_firstStartFalse_networkRequestInProgress() {
        when(interactor.networkRequestInProgress()).thenReturn(true);

        presenter.onStart(false);

        verify(interactor, times(1)).networkRequestInProgress();
        verify(view, times(1)).showLoadingIndicator();
        verify(view, times(1)).showPokemonDetails(any(Pokemon.class));
        verifyNoMoreInteractions(view, interactor);
    }

    @Test
    public void testOnPresenterDestroyed() {
        presenter.onPresenterDestroyed();

        verify(interactor, times(1)).cancelNetworkRequest();
        verifyNoMoreInteractions(view, interactor);
    }

    @Test
    public void testGetPokemonDetails() {
        int pokemonId = 25;

        presenter.getPokemonDetails(pokemonId);

        verify(view, times(1)).showLoadingIndicator();
        verify(interactor, times(1)).getPokemonDetails(integerCaptor.capture(), eq(presenter));
        verifyNoMoreInteractions(view, interactor);

        assertThat(integerCaptor.getValue(), is(pokemonId));
    }

    @Test
    public void testOnGetPokemonDetailsListener_OnSuccess() {
        Pokemon pokemon = new Pokemon();
        pokemon.id = 25;
        pokemon.name = "Pikachu";
        pokemon.height = 4;
        pokemon.weight = 60;
        pokemon.baseExperience = 112;
        pokemon.sprites = new PokemonSprites();
        pokemon.sprites.frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png";
        pokemon.sprites.backDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/25.png";

        presenter.onSuccess(pokemon);

        verify(view, times(1)).showPokemonDetails(pokemonCaptor.capture());
        verify(view, times(1)).hideLoadingIndicator();
        verify(view, times(1)).showDoneMessage();
        verifyNoMoreInteractions(view, interactor);

        assertThat(pokemonCaptor.getValue(), is(pokemon));
    }

    @Test
    public void testOnGetPokemonDetailsListener_OnFailure() {
        presenter.onFailure();

        verify(view, times(1)).hideLoadingIndicator();
        verify(view, times(1)).showErrorMessage();
        verifyNoMoreInteractions(view, interactor);
    }

}
