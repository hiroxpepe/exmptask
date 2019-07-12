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

import org.apache.commons.collections.Closure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author hiroxpepe
 */
public class MockJobClosure implements Closure {

    private final Log LOG = LogFactory.getLog(
        MockJobClosure.class
    );

    ///////////////////////////////////////////////////////////////////////////
    // public methods

    @Override
    public void execute(Object o) {
        LOG.info("called.");
        try {
            System.out.println("mock job.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
