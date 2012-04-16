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

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Factory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author hiroxpepe
 */
public class ArgumentBeanFactory implements Factory {

    private final Log LOG = LogFactory.getLog(
        ArgumentBeanFactory.class
    );

    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    @Override
    public Object create() {
        LOG.debug("called.");
        try {
            // create a dynaproperty array.
            DynaProperty[] props = new DynaProperty[3];
            
            // create a dynaproperty object.
            props[0] = new DynaProperty(
                "count",
                Integer.class
            );
            props[1] = new DynaProperty(
                "job",
                Closure.class
            );
            props[2] = new DynaProperty(
                "state",
                DynaBean.class
            );
            
            // create a dynaclass object.
            DynaClass clazz = new BasicDynaClass(
                "argument", 
                BasicDynaBean.class,
                props
            );
            
            // create a dynabean object.
            DynaBean bean = clazz.newInstance();
            
            // return the dynabean object.
            return bean;
            
        } catch(Exception e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
