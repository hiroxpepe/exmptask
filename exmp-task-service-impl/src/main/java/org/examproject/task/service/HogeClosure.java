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

package org.examproject.task.service;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.Closure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * the simple moke class.
 * @author hiroxpepe
 */
public class HogeClosure implements Closure {

    private static final Log LOG = LogFactory.getLog(
        HogeClosure.class
    );

    private final String waitTime;

    public HogeClosure (String waitTime) {
        LOG.debug("called.");
        
        this.waitTime = waitTime;
        LOG.info("waitTime : " + waitTime);
    }

    @Override
    public void execute(final Object o) {
        LOG.debug("called.");
        try{
            // the argument object is dynabean object.
            DynaBean state = (DynaBean) o;
            
            // mock task..
            Thread.sleep(
                Long.parseLong(waitTime)
            );
        } catch (Exception e) {
            LOG.error("error: " + e.getMessage());
            throw new RuntimeException(e);
        }
        LOG.info("hoge ok.");
    }
}
