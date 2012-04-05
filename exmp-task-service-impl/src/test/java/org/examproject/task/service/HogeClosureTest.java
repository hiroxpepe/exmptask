
package org.examproject.task.service;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * @author hiroxpepe
 */
public class HogeClosureTest {
    
    public HogeClosureTest() {
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
        Object o = null;
        HogeClosure instance = new HogeClosure("1");
        instance.execute(o);
    }
}
