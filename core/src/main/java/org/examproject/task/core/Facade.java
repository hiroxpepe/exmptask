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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Factory;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author hiroxpepe
 */
@Slf4j
@Service
public class Facade implements Runnable {

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

    private final Closure jobClosure;

    private AtomicInteger counter = new AtomicInteger();

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
        Closure jobClosure
    ) {
        this.argumentBeanFactory = argumentBeanFactory;
        this.stateBeanFactory = stateBeanFactory;
        this.paramBeanFactory = paramBeanFactory;
        this.resultBeanFactory = resultBeanFactory;
        this.contentListFactory = contentListFactory;
        this.workerBeanId = workerBeanId;
        this.jobClosure = jobClosure;
    }

    public Facade(
        Factory contentListFactory,
        String workerBeanId,
        Closure jobClosure
    ) {
        this.argumentBeanFactory = new ArgumentBeanFactory();
        this.stateBeanFactory = new StateBeanFactory();
        this.paramBeanFactory = new ParamBeanFactory();
        this.resultBeanFactory = new ResultBeanFactory();
        this.contentListFactory = contentListFactory;
        this.workerBeanId = workerBeanId;
        this.jobClosure = jobClosure;
    }

    public Facade(
        Factory contentListFactory,
        Closure jobClosure
    ) {
        this.argumentBeanFactory = new ArgumentBeanFactory();
        this.stateBeanFactory = new StateBeanFactory();
        this.paramBeanFactory = new ParamBeanFactory();
        this.resultBeanFactory = new ResultBeanFactory();
        this.contentListFactory = contentListFactory;
        this.workerBeanId = "simpleWorker";
        this.jobClosure = jobClosure;
    }

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public void run() {
        log.debug("called.");

        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> facade begin.");
            log.info("processing at " + new Date());

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            // initialize the content object for this run.
            init();

            // set the param for the worker object of the list.
            List<Runnable> workerList = new CopyOnWriteArrayList<Runnable>();
            for (int i = 0; i < contentList.size(); i++) {

                // create the beans of result for the worker.
                DynaBean result = (DynaBean) resultBeanFactory.create();

                // create the beans of state for the worker.
                DynaBean state = (DynaBean) stateBeanFactory.create();

                // create the beans of argument for the worker.
                DynaBean argument = (DynaBean) argumentBeanFactory.create();

                // build the parameter for the worker.
                state.set("result", result);
                state.set("param", getParam());
                argument.set("job",jobClosure);
                argument.set("state",state);
                argument.set("count", counter.incrementAndGet());

                // set the argument object for the worker.
                Runnable worker = (Runnable) context.getBean(
                    workerBeanId,
                    argument
                );

                // add the worker to the list.
                workerList.add(worker);
            }

            // run the all of the worker object.
            for (int i = 0; i < workerList.size(); i++) {
                executor.execute(
                    workerList.get(i)
                );
            }

            stopWatch.stop();

            log.info("execute time: " + stopWatch.getTime() + " msec");  
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< facade end.");

        } catch (Exception e) {
            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< facade error.");
            log.error("exception occurred. " + e.getMessage());

            // TODO: final strategy of the error!
            throw new RuntimeException(e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods

    private void init() {

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
