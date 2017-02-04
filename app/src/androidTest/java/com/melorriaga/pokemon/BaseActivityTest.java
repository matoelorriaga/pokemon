package com.melorriaga.pokemon;

import org.junit.After;
import org.junit.Before;

import static io.appflate.restmock.RESTMockServer.reset;

/**
 * Created by melorriaga on 4/2/17.
 */
public abstract class BaseActivityTest {

    @Before
    public void setUp() throws Exception {
        reset();
    }

    @After
    public void tearDown() throws Exception {
        Thread.sleep(3000);
    }

}
