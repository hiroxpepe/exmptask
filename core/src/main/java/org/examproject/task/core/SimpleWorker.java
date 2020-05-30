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
 * a worker object is only to run the job object.
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
        Closure job = (Closure) argument.get("job");
        Integer count = (Integer) argument.get("count");
        DynaBean state = (DynaBean) argument.get("state");

        // the job object is must be set.
        if (job == null) {
            String msg = "job is must be set.";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        // the state object is must be set for run.
        if (state == null) {
            log.warn("state is null.");
            return;
        }

        String threadName = Thread.currentThread().getName();
        log.info("▼ " + threadName + " beginning worker on " + count);

        try {
            // execute the job object given the state object.
            job.execute(
                state
            );

        } catch (Exception e) {
            log.info("▲ " + threadName + " error worker on " + count);
            log.error("exception occurred. " + e.getMessage());
            throw new RuntimeException(e);
        }

        log.info("▲ " + threadName + " completed worker on " + count);
    }
}
