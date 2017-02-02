package com.melorriaga.pokemon.view.impl;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.melorriaga.pokemon.R;
import com.melorriaga.pokemon.injection.component.AppComponent;
import com.melorriaga.pokemon.injection.component.DaggerDetailsComponent;
import com.melorriaga.pokemon.injection.module.DetailsModule;
import com.melorriaga.pokemon.model.Pokemon;
import com.melorriaga.pokemon.presenter.DetailsPresenter;
import com.melorriaga.pokemon.presenter.loader.PresenterFactory;
import com.melorriaga.pokemon.view.DetailsView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class DetailsActivity extends BaseActivity<DetailsPresenter, DetailsView> implements DetailsView {

    @Inject
    Resources mResources;

    @Inject
    PresenterFactory<DetailsPresenter> mPresenterFactory;

    @InjectExtra("pokemonId")
    int mPokemonId;

    @InjectExtra("pokemonName")
    String mPokemonName;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.pokemon_details_layout)
    LinearLayout mPokemonDetailsLayout;

    @BindView(R.id.pokemon_front_image)
    ImageView mPokemonFrontImageView;

    @BindView(R.id.pokemon_back_image)
    ImageView mPokemonBackImageView;

    @BindView(R.id.pokemon_id)
    TextView mPokemonIdTextView;

    @BindView(R.id.pokemon_name)
    TextView mPokemonNameTextView;

    @BindView(R.id.pokemon_height)
    TextView mPokemonHeightTextView;

    @BindView(R.id.pokemon_weight)
    TextView mPokemonWeightTextView;

    @BindView(R.id.pokemon_base_experience)
    TextView mPokemonBaseExperienceTextView;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerDetailsComponent.builder()
                .appComponent(parentComponent)
                .detailsModule(new DetailsModule())
                .build()
                .inject(this);
    }

    @NonNull
    @Override
    protected PresenterFactory<DetailsPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        Dart.inject(this);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.

        initToolbar();
        setToolbarTitle(String.format("%s (#%s)", mPokemonName, mPokemonId));
    }

    @Override
    public void showLoadingIndicator() {
        mProgressBar.setVisibility(View.VISIBLE);
        mPokemonDetailsLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoadingIndicator() {
        mProgressBar.setVisibility(View.GONE);
        mPokemonDetailsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public int getPokemonId() {
        return mPokemonId;
    }

    @Override
    public void showPokemonDetails(Pokemon pokemon) {
        if (pokemon != null) {
            Picasso.with(this).load(pokemon.sprites.frontDefault).into(mPokemonFrontImageView);
            Picasso.with(this).load(pokemon.sprites.backDefault).into(mPokemonBackImageView);
            mPokemonIdTextView.setText(mResources.getString(R.string.pokemon_id, pokemon.id));
            mPokemonNameTextView.setText(mResources.getString(R.string.pokemon_name, pokemon.name));
            mPokemonHeightTextView.setText(mResources.getString(R.string.pokemon_height, pokemon.height));
            mPokemonWeightTextView.setText(mResources.getString(R.string.pokemon_weight, pokemon.weight));
            mPokemonBaseExperienceTextView.setText(mResources.getString(R.string.pokemon_base_experience, pokemon.baseExperience));
        }
    }

    @Override
    public void showDoneMessage() {
        Snackbar.make(mCoordinatorLayout,R.string.done, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage() {
        Snackbar.make(mCoordinatorLayout,R.string.error, Snackbar.LENGTH_SHORT)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.getPokemonDetails(mPokemonId);
                    }
                })
                .show();
    }

}
