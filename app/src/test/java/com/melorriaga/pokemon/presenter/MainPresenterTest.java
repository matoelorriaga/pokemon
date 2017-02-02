package com.melorriaga.pokemon.presenter;

import com.melorriaga.pokemon.interactor.MainInteractor;
import com.melorriaga.pokemon.presenter.impl.MainPresenterImpl;
import com.melorriaga.pokemon.view.MainView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by melorriaga on 2/2/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    MainView view;

    @Mock
    MainInteractor interactor;

    @Captor
    ArgumentCaptor<List<String>> listStringCaptor;

    @InjectMocks
    MainPresenterImpl presenter;

    @Before
    public void setUp() throws Exception {
        Whitebox.setInternalState(presenter, "mView", view);
    }

    @Test
    public void testOnStart_firstStartTrue() {
        presenter.onStart(true);

        verify(view, times(1)).showLoadingIndicator();
        verify(interactor, times(1)).getPokemonNames(presenter);
        verifyNoMoreInteractions(view, interactor);
    }

    @Test
    public void testOnStart_firstStartFalse() {
        when(interactor.networkRequestInProgress()).thenReturn(false);

        presenter.onStart(false);

        verify(interactor, times(1)).networkRequestInProgress();
        verify(view, times(1)).showPokemonNames(anyList());
        verifyNoMoreInteractions(view, interactor);
    }

    @Test
    public void testOnStart_firstStartFalse_networkRequestInProgress() {
        when(interactor.networkRequestInProgress()).thenReturn(true);

        presenter.onStart(false);

        verify(interactor, times(1)).networkRequestInProgress();
        verify(view, times(1)).showLoadingIndicator();
        verify(view, times(1)).showPokemonNames(anyList());
        verifyNoMoreInteractions(view, interactor);
    }

    @Test
    public void testOnPresenterDestroyed() {
        presenter.onPresenterDestroyed();

        verify(interactor, times(1)).cancelNetworkRequest();
        verifyNoMoreInteractions(view, interactor);
    }

    @Test
    public void testGetPokemonNames() {
        presenter.getPokemonNames();

        verify(view, times(1)).showLoadingIndicator();
        verify(interactor, times(1)).getPokemonNames(presenter);
        verifyNoMoreInteractions(view, interactor);
    }

    @Test
    public void testOnGetPokemonNamesListener_OnSuccess() {
        List<String> pokemonNames = Arrays.asList("bulbasaur", "charmander", "squirtle");

        presenter.onSuccess(pokemonNames);

        verify(view, times(1)).showPokemonNames(listStringCaptor.capture());
        verify(view, times(1)).hideLoadingIndicator();
        verify(view, times(1)).showDoneMessage();
        verifyNoMoreInteractions(view, interactor);

        assertThat(listStringCaptor.getValue(), is(pokemonNames));
    }

    @Test
    public void testOnGetPokemonNamesListener_OnFailure() {
        presenter.onFailure();

        verify(view, times(1)).hideLoadingIndicator();
        verify(view, times(1)).showErrorMessage();
        verifyNoMoreInteractions(view, interactor);
    }

}
