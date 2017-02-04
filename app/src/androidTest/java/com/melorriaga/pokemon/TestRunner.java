package com.melorriaga.pokemon;

import android.app.Application;
import android.content.Context;

import io.appflate.restmock.android.RESTMockTestRunner;

/**
 * Created by melorriaga on 4/2/17.
 */
public class TestRunner extends RESTMockTestRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, TestPokemonApp.class.getName(), context);
    }

}
