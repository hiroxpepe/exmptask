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

import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.Closure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.examproject.task.dto.HogeDto;

/**
 * a simple mock class.
 * @author hiroxpepe
 */
public class HogeClosure implements Closure {

    private static final Log LOG = LogFactory.getLog(
        HogeClosure.class
    );

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public void execute(Object o) {
        LOG.debug("called.");
        try{
            // the argument object is dynabean object.
            DynaBean state = (DynaBean) o;

            // get the value map from the param object.
            DynaBean param = (DynaBean) state.get("param");
            Map<String, HogeDto> values = (Map<String, HogeDto>) param.get("values");

            // get the content object.
            HogeDto content = (HogeDto) values.get("content");
            if (content == null) {
                LOG.warn("content is null.");
                return;
            }

            // mock task..
            LOG.debug("waitTime: " + content.getWaitTime().toString());
            Thread.sleep(
                Long.parseLong(content.getWaitTime())
            );
            LOG.debug(content.getName() + " ok.");

        } catch (Exception e) {
            LOG.error("error: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
