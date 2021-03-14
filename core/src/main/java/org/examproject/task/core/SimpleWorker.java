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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.Closure;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * a worker object is only to run the _job object.
 *
 * @author hiroxpepe
 */
@Slf4j
@Component(value="simpleWorker")
@Scope(value="prototype")
public class SimpleWorker implements Runnable {

    private final DynaBean argument;

    ///////////////////////////////////////////////////////////////////////////
    // constructor

    public SimpleWorker() {
        argument = null;
    }

    public SimpleWorker(DynaBean argument) {
        this.argument = argument;
    }

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public void run() {
        log.debug("called.");
        if (argument == null) {
            log.warn("argument is null.");
            return;
        }

        // get the object from argument object.
        Closure _job = (Closure) argument.get("job");
        Integer _count = (Integer) argument.get("count");
        DynaBean _state = (DynaBean) argument.get("state");

        // the _job object is must be set.
        if (_job == null) {
            String _msg = "job is must be set.";
            log.error(_msg);
            throw new IllegalArgumentException(_msg);
        }

        // the _state object is must be set for run.
        if (_state == null) {
            log.warn("state is null.");
            return;
        }

        String _threadName = Thread.currentThread().getName();
        log.info("▼ " + _threadName + " beginning worker on " + _count);

        try {
            // execute the _job object given the _state object.
            _job.execute(_state
            );

        } catch (Exception e) {
            log.info("▲ " + _threadName + " error worker on " + _count);
            log.error("exception occurred. " + e.getMessage());
            throw new RuntimeException(e);
        }

        log.info("▲ " + _threadName + " completed worker on " + _count);
    }
}
