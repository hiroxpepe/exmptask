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

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.Closure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * a worker object is only to run the job object.
 * 
 * @author hiroxpepe
 */
@Component(value="simpleWorker")
@Scope(value="prototype")
public class SimpleWorker implements Runnable {

    private final Log LOG = LogFactory.getLog(
        SimpleWorker.class
    );

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
        LOG.debug("called.");
        if (argument == null) {
            LOG.warn("argument is null.");
            return;
        }

        // get the object from argument object.
        Closure job = (Closure) argument.get("job");
        Integer count = (Integer) argument.get("count");
        DynaBean state = (DynaBean) argument.get("state");

        // the job object is must be set.
        if (job == null) {
            String msg = "job is must be set.";
            LOG.error(msg);
            throw new IllegalArgumentException(msg);
        }

        String threadName = Thread.currentThread().getName();
        LOG.info("▼ " + threadName + " beginning worker on " + count);
        
        try {
            // execute the job object given the state object.
            job.execute(
                state
            );

        } catch (Exception e) {
            LOG.info("▲ " + threadName + " error worker on " + count);
            LOG.error("exception occurred. " + e.getMessage());
            throw new RuntimeException(e);
        }
        
        LOG.info("▲ " + threadName + " completed worker on " + count);
    }
}
