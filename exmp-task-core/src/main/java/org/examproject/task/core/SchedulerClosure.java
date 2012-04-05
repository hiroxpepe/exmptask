/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.examproject.task.core;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Factory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author hiroxpepe
 */
@Service
public class SchedulerClosure implements Closure {

    private final Log LOG = LogFactory.getLog(
        SchedulerClosure.class
    );

    @Inject
    private final ApplicationContext context = null;

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
    
    private final Closure worker;

    private final Closure job;

    private AtomicInteger counter = new AtomicInteger();
    
    private DynaBean argument;
            
    private DynaBean state;

    private DynaBean param;
            
    private DynaBean result;

    private boolean isInit = false;
    
    ///////////////////////////////////////////////////////////////////////////
    // constructor

    public SchedulerClosure(
        Closure worker,
        Closure job
    ) {
        this.worker = worker;
        this.job = job;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public void execute(Object o) {
        LOG.debug("called.");
        try {
            LOG.info("▼▼ beginning scheduler.");
            
            // initialize the fields. 
            init();
            
            LOG.info("processing at " + new Date());
            
            // set the execute counts.
            argument.set(
                "count",
                counter.incrementAndGet()
            );
            
            // execute the worker object given the argument object.
            worker.execute(
                argument
            );
            
            LOG.info("▲▲ completed scheduler.");
            
        } catch (Exception e) {
            LOG.info("▲▲ error scheduler.");
            LOG.error("exception occurred. " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // private methods
    
    private void init() {
        if (isInit) {
            return;
        }
        
        // create the beans of application use. 
        argument = (DynaBean) argumentBeanFactory.create();
        state = (DynaBean) stateBeanFactory.create();
        param = (DynaBean) paramBeanFactory.create();
        result = (DynaBean) resultBeanFactory.create();
        
        // set the parameter to the beans.
        state.set("param", param);
        state.set("result", result);
        argument.set("job",job);
        argument.set("state",state);
        
        // initialize function is executed only once.
        isInit = true;
    }
}
