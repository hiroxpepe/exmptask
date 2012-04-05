
package org.examproject.task.core;

import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Factory;
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
public class AsyncWorkerClosureTest {

    @Inject
    private ApplicationContext context;

    @Inject
    @Named(value="instance")
    private Closure instance;

    @Inject
    @Named(value="argumentBeanFactory")
    private Factory argumentBeanFactory;
    
    @Inject
    @Named(value="stateBeanFactory")
    private Factory stateBeanFactory;

    @Inject
    @Named(value="paramBeanFactory")
    private Factory paramBeanFactory;
    
    @Inject
    @Named(value="resultBeanFactory")
    private Factory resultBeanFactory;

    @Inject
    @Named(value="mockJob")
    private Closure job;

    public AsyncWorkerClosureTest() {
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
        
        DynaBean argument = (DynaBean) argumentBeanFactory.create();
        DynaBean state = (DynaBean) stateBeanFactory.create();
        DynaBean param = (DynaBean) paramBeanFactory.create();
        DynaBean result = (DynaBean) resultBeanFactory.create();

        AtomicInteger counter = new AtomicInteger();

        state.set("param", param);
        state.set("result", result);
        argument.set("count", counter.incrementAndGet());
        argument.set("job", job);
        argument.set("state", state);

        instance.execute(argument);
    }

}