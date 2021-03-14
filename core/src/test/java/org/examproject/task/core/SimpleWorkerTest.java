
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

/**
 * @author hiroxpepe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SimpleWorkerTest {

    @Inject
    private ApplicationContext context;

    private Runnable instance;

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

    public SimpleWorkerTest() {
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

        DynaBean _argument = (DynaBean) argumentBeanFactory.create();
        DynaBean _state = (DynaBean) stateBeanFactory.create();
        DynaBean _param = (DynaBean) paramBeanFactory.create();
        DynaBean _result = (DynaBean) resultBeanFactory.create();

        AtomicInteger _counter = new AtomicInteger();

        _state.set("param", _param);
        _state.set("result", _result);
        _argument.set("count", _counter.incrementAndGet());
        _argument.set("job", job);
        _argument.set("state", _state);

        instance = (Runnable) context.getBean("instance", _argument);
        instance.run();
    }
}
