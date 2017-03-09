package com.melorriaga.pokemon

import io.appflate.restmock.RESTMockServer.reset
import org.junit.After
import org.junit.Before

/**
 * Created by melorriaga on 4/2/17.
 */
abstract class BaseActivityTest {

    @Before
    @Throws(Exception::class)
    fun setUp() {
        reset()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Thread.sleep(1000)
    }

}
