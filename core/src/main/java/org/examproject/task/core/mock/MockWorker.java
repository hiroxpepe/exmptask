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

package org.examproject.task.core.mock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.Closure;

/**
 * @author hiroxpepe
 */
@Slf4j
public class MockWorker implements Runnable {

    private final DynaBean argument;

    ///////////////////////////////////////////////////////////////////////////
    // constructor

    public MockWorker(DynaBean argument) {
        this.argument = argument;
    }

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public void run() {
        log.info("called.");
        try {

            // get the _state object from the parameter.
            DynaBean _state = (DynaBean) argument.get(
                "state"
            );

            // get the _job object from the parameter.
            Closure _job = (Closure) argument.get(
                "job"
            );

            // executes it by passing a _state object to the _job object.
            _job.execute(_state);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
