package com.melorriaga.pokemon.presenter

import com.melorriaga.pokemon.interactor.MainInteractor
import com.melorriaga.pokemon.presenter.impl.MainPresenterImpl
import com.melorriaga.pokemon.view.MainView
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Matchers.anyListOf
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.internal.util.reflection.Whitebox
import java.util.*

/**
 * Created by melorriaga on 2/2/17.
 */
class MainPresenterTest : BasePresenterTest() {

    @Mock
    lateinit var view: MainView

    @Mock
    lateinit var interactor: MainInteractor

    @Captor
    lateinit var listStringCaptor: ArgumentCaptor<List<String>>

    @InjectMocks
    lateinit var presenter: MainPresenterImpl

    @Before
    @Throws(Exception::class)
    fun setUp() {
        Whitebox.setInternalState(presenter, "mView", view)
    }

    @Test
    fun testOnStart_firstStartTrue() {
        presenter.onStart(true)

        verify(view, times(1)).showLoadingIndicator()
        verify(interactor, times(1)).getPokemonNames(presenter)
        verifyNoMoreInteractions(view, interactor)
    }

    @Test
    fun testOnStart_firstStartFalse() {
        `when`(interactor.networkRequestInProgress()).thenReturn(false)

        presenter.onStart(false)

        verify(interactor, times(1)).networkRequestInProgress()
        verify(view, times(1)).showPokemonNames(anyListOf(String::class.java))
        verifyNoMoreInteractions(view, interactor)
    }

    @Test
    fun testOnStart_firstStartFalse_networkRequestInProgress() {
        `when`(interactor.networkRequestInProgress()).thenReturn(true)

        presenter.onStart(false)

        verify(interactor, times(1)).networkRequestInProgress()
        verify(view, times(1)).showLoadingIndicator()
        verify(view, times(1)).showPokemonNames(anyListOf(String::class.java))
        verifyNoMoreInteractions(view, interactor)
    }

    @Test
    fun testOnPresenterDestroyed() {
        presenter.onPresenterDestroyed()

        verify(interactor, times(1)).cancelNetworkRequest()
        verifyNoMoreInteractions(view, interactor)
    }

    @Test
    fun testGetPokemonNames() {
        presenter.getPokemonNames()

        verify(view, times(1)).showLoadingIndicator()
        verify(interactor, times(1)).getPokemonNames(presenter)
        verifyNoMoreInteractions(view, interactor)
    }

    @Test
    fun testOnGetPokemonNamesListener_OnSuccess() {
        val pokemonNames = Arrays.asList("bulbasaur", "charmander", "squirtle")

        presenter.onSuccess(pokemonNames)

        verify(view, times(1)).showPokemonNames(listStringCaptor.capture())
        verify(view, times(1)).hideLoadingIndicator()
        verify(view, times(1)).showDoneMessage()
        verifyNoMoreInteractions(view, interactor)

        assertThat(listStringCaptor.value, `is`(pokemonNames))
    }

    @Test
    fun testOnGetPokemonNamesListener_OnFailure() {
        presenter.onFailure()

        verify(view, times(1)).hideLoadingIndicator()
        verify(view, times(1)).showErrorMessage()
        verifyNoMoreInteractions(view, interactor)
    }

}
