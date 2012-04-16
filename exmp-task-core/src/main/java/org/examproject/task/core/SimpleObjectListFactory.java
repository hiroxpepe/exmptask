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

import java.util.List;
import org.apache.commons.collections.Factory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author hiroxpepe
 */
public class SimpleObjectListFactory implements Factory {
    
    private final Log LOG = LogFactory.getLog(
        SimpleObjectListFactory.class
    );
    
    private final List objectList;
    
    ///////////////////////////////////////////////////////////////////////////
    // constructor
    
    public SimpleObjectListFactory(List objectList) {
        this.objectList = objectList;
    }
    
    ///////////////////////////////////////////////////////////////////////////
    // public methods
    
    @Override
    public Object create() {
        LOG.debug("called.");
        try {
            // return the object list.
            return objectList;
            
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
}
