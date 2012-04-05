
package org.examproject.task.core;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.Closure;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.junit.runner.RunWith;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author hiroxpepe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SchedulerClosureTest {

    @Inject
    private ApplicationContext context;

    @Inject
    @Named(value="instance")
    private Closure instance;

    public SchedulerClosureTest() {
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
    public void testExecute() {
        System.out.println("execute");
        instance.execute(null);
    }

}