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

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.collections.Closure;

import org.examproject.task.dto.HogeDto;

/**
 * a simple mock class.
 * @author hiroxpepe
 */
@Slf4j
public class HogeClosure implements Closure {

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public void execute(Object o) {
        log.info("called.");
        try{
            // the argument object is dynabean object.
            DynaBean _state = (DynaBean) o;

            // get the value map from the _param object.
            DynaBean _param = (DynaBean) _state.get("param");
            Map<String, HogeDto> _values = (Map<String, HogeDto>) _param.get("values");

            // get the _content object.
            HogeDto _content = (HogeDto) _values.get("content");
            if (_content == null) {
                log.warn("content is null.");
                return;
            }

            // mock task..
            log.info("waitTime: " + _content.getWaitTime());
            Thread.sleep(Long.parseLong(_content.getWaitTime()));
            log.info(_content.getName() + " ok.");

        } catch (Exception e) {
            log.error("error: " + e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
