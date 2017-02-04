package com.melorriaga.pokemon.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.melorriaga.pokemon.BaseActivityTest;
import com.melorriaga.pokemon.R;
import com.melorriaga.pokemon.view.impl.DetailsActivity;

import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static io.appflate.restmock.RESTMockServer.whenGET;
import static io.appflate.restmock.RequestsVerifier.verifyGET;
import static io.appflate.restmock.utils.RequestMatchers.pathEndsWith;
import static org.hamcrest.CoreMatchers.not;

/**
 * Created by melorriaga on 4/2/17.
 */
public class DetailsActivityTest extends BaseActivityTest {

    @Rule
    public ActivityTestRule<DetailsActivity> detailsActivityTestRule = new ActivityTestRule<>(
            DetailsActivity.class,  // activityClass
            true,                   // initialTouchMode
            false                   // launchActivity
    );

    @Test
    public void testShowPokemonDetails_success() {
        whenGET(pathEndsWith("pokemon/25/"))
                .delay(TimeUnit.SECONDS, 1)
                .thenReturnFile(200, "getPokemonDetails_200.json");

        Intent intent = new Intent();
        intent.putExtra("pokemonId", 25);
        intent.putExtra("pokemonName", "pikachu");
        detailsActivityTestRule.launchActivity(intent);

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.pokemon_details_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.pokemon_id)).check(matches(withText("id: 25")));
        onView(withId(R.id.pokemon_name)).check(matches(withText("name: pikachu")));
        onView(withText(R.string.done)).check(matches(withEffectiveVisibility(VISIBLE)));

        verifyGET(pathEndsWith("pokemon/25/")).invoked();
    }

    @Test
    public void testShowPokemonDetails_error() {
        whenGET(pathEndsWith("pokemon/25/"))
                .delay(TimeUnit.SECONDS, 1)
                .thenReturnEmpty(404);

        Intent intent = new Intent();
        intent.putExtra("pokemonId", 25);
        intent.putExtra("pokemonName", "pikachu");
        detailsActivityTestRule.launchActivity(intent);

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.pokemon_details_layout)).check(matches(isDisplayed()));
        onView(withText(R.string.error)).check(matches(withEffectiveVisibility(VISIBLE)));

        verifyGET(pathEndsWith("pokemon/25/")).invoked();
    }

    @Test
    public void testShowPokemonDetails_error_retry() {
        whenGET(pathEndsWith("pokemon/25/"))
                .delay(TimeUnit.SECONDS, 1)
                .thenReturnEmpty(404)
                .delay(TimeUnit.SECONDS, 1)
                .thenReturnFile(200, "getPokemonDetails_200.json");

        Intent intent = new Intent();
        intent.putExtra("pokemonId", 25);
        intent.putExtra("pokemonName", "pikachu");
        detailsActivityTestRule.launchActivity(intent);

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withText(R.string.error)).check(matches(withEffectiveVisibility(VISIBLE)));
        onView(withText(R.string.retry)).perform(click());
        onView(withId(R.id.pokemon_details_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.pokemon_id)).check(matches(withText("id: 25")));
        onView(withId(R.id.pokemon_name)).check(matches(withText("name: pikachu")));
        onView(withText(R.string.done)).check(matches(withEffectiveVisibility(VISIBLE)));

        verifyGET(pathEndsWith("pokemon/25/")).exactly(2);
    }

}
