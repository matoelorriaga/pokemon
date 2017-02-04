package com.melorriaga.pokemon.activity;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.melorriaga.pokemon.BaseActivityTest;
import com.melorriaga.pokemon.R;
import com.melorriaga.pokemon.assertion.RecyclerViewItemCountAssertion;
import com.melorriaga.pokemon.view.impl.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static io.appflate.restmock.RESTMockServer.whenGET;
import static io.appflate.restmock.RequestsVerifier.verifyGET;
import static io.appflate.restmock.utils.RequestMatchers.pathEndsWith;

/**
 * Created by melorriaga on 3/2/17.
 */
public class MainActivityTest extends BaseActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(
            MainActivity.class,     // activityClass
            true,                   // initialTouchMode
            false                   // launchActivity
    );

    @Test
    public void testShowPokemonNames_success() {
        whenGET(pathEndsWith("pokemon?limit=150"))
                .thenReturnFile(200, "getPokemonNames_200.json");

        Intent intent = new Intent();
        mainActivityTestRule.launchActivity(intent);

        onView(withId(R.id.recycler_view))
                .check(new RecyclerViewItemCountAssertion(150));
        onView(withText(R.string.done))
                .check(matches(withEffectiveVisibility(VISIBLE)));

        verifyGET(pathEndsWith("pokemon?limit=150")).invoked();
    }

    @Test
    public void testShowPokemonNames_error() {
        whenGET(pathEndsWith("pokemon?limit=150"))
                .thenReturnEmpty(404);

        Intent intent = new Intent();
        mainActivityTestRule.launchActivity(intent);

        onView(withId(R.id.recycler_view))
                .check(new RecyclerViewItemCountAssertion(0));
        onView(withText(R.string.error))
                .check(matches(withEffectiveVisibility(VISIBLE)));

        verifyGET(pathEndsWith("pokemon?limit=150")).invoked();
    }

}
