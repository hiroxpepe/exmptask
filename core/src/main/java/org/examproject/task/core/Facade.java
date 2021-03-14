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

    private final AtomicInteger counter = new AtomicInteger();

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

            StopWatch _stopWatch = new StopWatch();
            _stopWatch.start();

            // initialize the content object for this run.
            init();

            // set the _param for the _worker object of the list.
            List<Runnable> _workerList = new CopyOnWriteArrayList<>();
            for (int _idx = 0; _idx < contentList.size(); _idx++) {

                // create the beans of _result for the _worker.
                DynaBean _result = (DynaBean) resultBeanFactory.create();

                // create the beans of _state for the _worker.
                DynaBean _state = (DynaBean) stateBeanFactory.create();

                // create the beans of _argument for the _worker.
                DynaBean _argument = (DynaBean) argumentBeanFactory.create();

                // build the parameter for the _worker.
                _state.set("result", _result);
                _state.set("param", getParam());
                _argument.set("job", jobClosure);
                _argument.set("state", _state);
                _argument.set("count", counter.incrementAndGet());

                // set the _argument object for the _worker.
                Runnable _worker = (Runnable) context.getBean(workerBeanId,
                    _argument
                );

                // add the _worker to the list.
                _workerList.add(_worker);
            }

            // run the all of the _worker object.
            for (int _idx = 0; _idx < _workerList.size(); _idx++) {
                executor.execute(_workerList.get(_idx));
            }

            _stopWatch.stop();

            log.info("execute time: " + _stopWatch.getTime() + " msec");
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
        Object _obj = contentList.remove(0);

        // set the object to map.
        DynaBean _param = (DynaBean) paramBeanFactory.create();
        Map<String, Object> _values = (Map<String, Object>) _param.get("values");
        _values.put("content", _obj);

        return _param;
    }
}
