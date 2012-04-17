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
import java.util.Iterator;
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
    
    private final Factory contentListFactory;
    
    private final String workerBeanId;

    private final Closure job;

    private AtomicInteger counter = new AtomicInteger();
            
    private DynaBean result;
    
    private List<Object> contentList;
    
    ///////////////////////////////////////////////////////////////////////////
    // constructor

    public Facade(
        Factory argumentBeanFactory,
        Factory stateBeanFactory,
        Factory paramBeanFactory,
        Factory resultBeanFactory,
        Factory contentListFactory,
        String workerBeanId,
        Closure job
    ) {
        this.argumentBeanFactory = argumentBeanFactory;
        this.stateBeanFactory = stateBeanFactory;
        this.paramBeanFactory = paramBeanFactory;
        this.resultBeanFactory = resultBeanFactory;
        this.contentListFactory = contentListFactory;
        this.workerBeanId = workerBeanId;
        this.job = job;
    }
    
    public Facade(
        Factory contentListFactory,
        String workerBeanId,
        Closure job
    ) {
        this.argumentBeanFactory = new ArgumentBeanFactory();
        this.stateBeanFactory = new StateBeanFactory();
        this.paramBeanFactory = new ParamBeanFactory();
        this.resultBeanFactory = new ResultBeanFactory();
        this.contentListFactory = contentListFactory;
        this.workerBeanId = workerBeanId;
        this.job = job;
    }
    
    public Facade(
        Factory contentListFactory,
        Closure job
    ) {
        this.argumentBeanFactory = new ArgumentBeanFactory();
        this.stateBeanFactory = new StateBeanFactory();
        this.paramBeanFactory = new ParamBeanFactory();
        this.resultBeanFactory = new ResultBeanFactory();
        this.contentListFactory = contentListFactory;
        this.workerBeanId = "simpleWorker";
        this.job = job;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public void run() {
        LOG.debug("called.");
        
        try {
            LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> facade begin.");
            LOG.info("processing at " + new Date());
          
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            
            // initialize the bean for this run.
            init();
            
            // set the param for the worker object of the list.
            List<Runnable> workerList = new CopyOnWriteArrayList<Runnable>();
            for (int i = 0; i < contentList.size(); i++) {
                    
                DynaBean state = (DynaBean) stateBeanFactory.create();
                state.set("result", result);
                state.set("param", getParam());
                
                // set the parameter to the beans.
                DynaBean argument = (DynaBean) argumentBeanFactory.create();
                argument.set("job",job);
                argument.set("state",state);
                
                // set the execute count.
                argument.set(
                    "count",
                    counter.incrementAndGet()
                );
                
                // set the argument object for worker object.
                Runnable worker = (Runnable) context.getBean(
                    workerBeanId,
                    argument
                );
                
                workerList.add(worker);
            }
            
            // run the all of the worker object.
            for (int i = 0; i < workerList.size(); i++) {
                executor.execute(
                    workerList.get(i)
                );
            }
            
            stopWatch.stop();
            
            LOG.info("execute time: " + stopWatch.getTime() + " msec");  
            LOG.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< facade end.");
            
        } catch (Exception e) {
            LOG.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< facade error.");
            LOG.error("exception occurred. " + e.getMessage());
            
            // TODO: final strategy of the error!
            throw new RuntimeException(e);
        }
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // private methods
    
    private void init() {
        
        // create the beans of result. 
        result = (DynaBean) resultBeanFactory.create();
        
        // get the content object list.
        contentList = (List<Object>) contentListFactory.create();
    }
    
    private DynaBean getParam() {
        
        // get the content object of the current.
        Object o = contentList.remove(0);
        
        // set the object to map.
        DynaBean param = (DynaBean) paramBeanFactory.create();
        Map<String, Object> values = (Map<String, Object>) param.get("values");
        values.put("content", o);
        
        return param;
    }
    
}
