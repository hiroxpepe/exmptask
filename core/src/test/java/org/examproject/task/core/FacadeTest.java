
package org.examproject.task.core;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author hiroxpepe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class FacadeTest {

    @Inject
    private ApplicationContext context;

    @Inject
    @Named(value="instance")
    private Runnable instance;

    public FacadeTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRun() {
        System.out.println("run");
        instance.run();
    }
}
