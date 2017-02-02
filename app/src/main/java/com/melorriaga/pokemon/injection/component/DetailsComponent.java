package com.melorriaga.pokemon.injection.component;

import com.melorriaga.pokemon.injection.module.DetailsModule;
import com.melorriaga.pokemon.injection.scope.ActivityScope;
import com.melorriaga.pokemon.view.impl.DetailsActivity;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = DetailsModule.class
)
public interface DetailsComponent {

    void inject(DetailsActivity activity);

}