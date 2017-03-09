package com.melorriaga.pokemon.presenter

import com.melorriaga.pokemon.interactor.DetailsInteractor
import com.melorriaga.pokemon.model.Pokemon
import com.melorriaga.pokemon.model.PokemonSprites
import com.melorriaga.pokemon.presenter.impl.DetailsPresenterImpl
import com.melorriaga.pokemon.view.DetailsView
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Matchers.anyInt
import org.mockito.Matchers.eq
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.internal.util.reflection.Whitebox

/**
 * Created by melorriaga on 3/2/17.
 */
class DetailsPresenterTest : BasePresenterTest() {

    @Mock
    lateinit var view: DetailsView

    @Mock
    lateinit var interactor: DetailsInteractor

    @Captor
    lateinit var integerCaptor: ArgumentCaptor<Int>

    @Captor
    lateinit var pokemonCaptor: ArgumentCaptor<Pokemon>

    @InjectMocks
    lateinit var presenter: DetailsPresenterImpl

    @Before
    @Throws(Exception::class)
    fun setUp() {
        Whitebox.setInternalState(presenter, "mView", view)
    }

    @Test
    fun testOnStart_firstStartTrue() {
        presenter.onStart(true)

        verify(view, times(1)).pokemonId
        verify(view, times(1)).showLoadingIndicator()
        verify(interactor, times(1)).getPokemonDetails(anyInt(), eq(presenter))
        verifyNoMoreInteractions(view, interactor)
    }

    @Test
    fun testOnStart_firstStartFalse() {
        `when`(interactor.networkRequestInProgress()).thenReturn(false)

        presenter.onStart(false)

        verify(interactor, times(1)).networkRequestInProgress()
        verify(view, times(1)).showPokemonDetails(any(Pokemon::class.java))
        verifyNoMoreInteractions(view, interactor)
    }

    @Test
    fun testOnStart_firstStartFalse_networkRequestInProgress() {
        `when`(interactor.networkRequestInProgress()).thenReturn(true)

        presenter.onStart(false)

        verify(interactor, times(1)).networkRequestInProgress()
        verify(view, times(1)).showLoadingIndicator()
        verify(view, times(1)).showPokemonDetails(any(Pokemon::class.java))
        verifyNoMoreInteractions(view, interactor)
    }

    @Test
    fun testOnPresenterDestroyed() {
        presenter.onPresenterDestroyed()

        verify(interactor, times(1)).cancelNetworkRequest()
        verifyNoMoreInteractions(view, interactor)
    }

    @Test
    fun testGetPokemonDetails() {
        val pokemonId = 25

        presenter.getPokemonDetails(pokemonId)

        verify(view, times(1)).showLoadingIndicator()
        verify(interactor, times(1)).getPokemonDetails(integerCaptor.capture(), eq(presenter))
        verifyNoMoreInteractions(view, interactor)

        assertThat(integerCaptor.value, `is`(pokemonId))
    }

    @Test
    fun testOnGetPokemonDetailsListener_OnSuccess() {
        val pokemon = Pokemon()
        pokemon.id = 25
        pokemon.name = "Pikachu"
        pokemon.height = 4
        pokemon.weight = 60
        pokemon.baseExperience = 112
        pokemon.sprites = PokemonSprites()
        pokemon.sprites.frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/25.png"
        pokemon.sprites.backDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/25.png"

        presenter.onSuccess(pokemon)

        verify(view, times(1)).showPokemonDetails(pokemonCaptor.capture())
        verify(view, times(1)).hideLoadingIndicator()
        verify(view, times(1)).showDoneMessage()
        verifyNoMoreInteractions(view, interactor)

        assertThat(pokemonCaptor.value, `is`(pokemon))
    }

    @Test
    fun testOnGetPokemonDetailsListener_OnFailure() {
        presenter.onFailure()

        verify(view, times(1)).hideLoadingIndicator()
        verify(view, times(1)).showErrorMessage()
        verifyNoMoreInteractions(view, interactor)
    }

}
