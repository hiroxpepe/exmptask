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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Factory;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author hiroxpepe
 */
@Service
public class Facade implements Runnable {

    private final Log LOG = LogFactory.getLog(
        Facade.class
    );

    @Inject
    private final ApplicationContext context = null;
    
    @Inject
    private final Executor executor = null;

    private final Factory argumentBeanFactory;
    
    private final Factory stateBeanFactory;

    private final Factory paramBeanFactory;
    
    private final Factory resultBeanFactory;
    
    private final Factory objectListFactory;
    
    private final String workerBeanId;

    private final Closure job;

    private AtomicInteger counter = new AtomicInteger();
    
    private DynaBean argument;
            
    private DynaBean state;

//    private DynaBean param;
            
    private DynaBean result;
    
    private List objectList;

    private boolean isInit = false;
    
    ///////////////////////////////////////////////////////////////////////////
    // constructor

    public Facade(
        Factory argumentBeanFactory,
        Factory stateBeanFactory,
        Factory paramBeanFactory,
        Factory resultBeanFactory,
        Factory objectListFactory,
        String workerBeanId,
        Closure job
    ) {
        this.argumentBeanFactory = argumentBeanFactory;
        this.stateBeanFactory = stateBeanFactory;
        this.paramBeanFactory = paramBeanFactory;
        this.resultBeanFactory = resultBeanFactory;
        this.objectListFactory = objectListFactory;
        this.workerBeanId = workerBeanId;
        this.job = job;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public void run() {
        LOG.debug("called.");
        
        try {
            LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> scheduler begin.");
            
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            
            LOG.info("processing at " + new Date());
            
            List<Runnable> workerList = new CopyOnWriteArrayList<Runnable>();
            for (int i = 0; i < objectList.size(); i++) {
                
                // set the execute count.
                argument.set(
                    "count",
                    counter.incrementAndGet()
                );
                
                state.set(
                    "param",
                    getParam()
                );
                
                // two arg is need.. 
                Runnable worker = (Runnable) context.getBean(
                    workerBeanId,
                    argument
                );
                
                workerList.add(worker);
            }
            
            for (int j = 0; j < workerList.size(); j++) {
                executor.execute(
                    workerList.get(j)
                );
            }
           
            stopWatch.stop();
            LOG.info("execute time: " + stopWatch.getTime() + " msec");
            
            LOG.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< scheduler end.");
            
        } catch (Exception e) {
            LOG.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< scheduler error.");
            LOG.error("exception occurred. " + e.getMessage());
            
            // TODO: final strategy of the error!
            throw new RuntimeException(e);
        }
    }
    
    public void init() {
        if (isInit) {
            return;
        }
        
        // create the beans of application use. 
        argument = (DynaBean) argumentBeanFactory.create();
        state = (DynaBean) stateBeanFactory.create();
        result = (DynaBean) resultBeanFactory.create();
        
        // get the object list.
        objectList = (List) objectListFactory.create();
        
        // set the parameter to the beans.
        state.set("result", result);
        argument.set("job",job);
        argument.set("state",state);
        
        // initialize function is executed only once.
        isInit = true;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // private methods
    
    private DynaBean getParam() {
        
        // get the current object.
        Object o = objectList.remove(0);
                
        //set the object to map.
        DynaBean param = (DynaBean) paramBeanFactory.create();
        Map<String, Object> values = (Map<String, Object>) param.get("values");
        values.put("item", o);
        
        return param;
    }
    
}
