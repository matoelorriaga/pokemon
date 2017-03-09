package com.melorriaga.pokemon

import android.app.Application
import android.content.Context

import io.appflate.restmock.android.RESTMockTestRunner

/**
 * Created by melorriaga on 4/2/17.
 */
class TestRunner : RESTMockTestRunner() {

    @Throws(InstantiationException::class, IllegalAccessException::class, ClassNotFoundException::class)
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestPokemonApp::class.java.name, context)
    }

}
