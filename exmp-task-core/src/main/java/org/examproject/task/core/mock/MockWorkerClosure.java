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

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.Closure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author hiroxpepe
 */
public class MockWorkerClosure  implements Closure {

    private final Log LOG = LogFactory.getLog(
        MockWorkerClosure.class
    );

    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    @Override
    public void execute(Object o) {
        LOG.info("called.");
        try {
            // argument object argument
            DynaBean argument = (DynaBean) o;
            
            // get the state object from the parameter.
            DynaBean state = (DynaBean) argument.get(
                "state"
            );
            
            // get the job object from the parameter.
            Closure job = (Closure) argument.get(
                "job"
            );
            
            // executes it by passing a state object to the job object.
            job.execute(
                state
            );
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
