package com.melorriaga.pokemon.injection.component;

import com.melorriaga.pokemon.injection.module.MainModule;
import com.melorriaga.pokemon.injection.scope.ActivityScope;
import com.melorriaga.pokemon.view.impl.MainActivity;

import dagger.Component;

@ActivityScope
@Component(
        dependencies = AppComponent.class,
        modules = MainModule.class
)
public interface MainComponent {

    void inject(MainActivity activity);

}