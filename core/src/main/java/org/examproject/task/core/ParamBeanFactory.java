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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.collections.Factory;

/**
 * @author hiroxpepe
 */
@Slf4j
public class ParamBeanFactory implements Factory {

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public Object create() {
        log.debug("called.");
        try {
            // create a dynaproperty array.
            DynaProperty[] props = new DynaProperty[1];

            // create a dynaproperty object.
            props[0] = new DynaProperty(
                "values",
                Map.class
            );

            // create a dynaclass object.
            DynaClass clazz = new BasicDynaClass(
                "param",
                BasicDynaBean.class,
                props
            );

            // create a dynabean object.
            DynaBean bean = clazz.newInstance();

            // create the values map.
            bean.set(
                "values",
                new ConcurrentHashMap<String, Object>()
            );

            // return the dynabean object.
            return bean;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
