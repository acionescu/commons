/*******************************************************************************
 * Copyright 2011 Adrian Cristian Ionescu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ro.zg.util.data;

import java.util.ArrayList;
import java.util.List;

public class MapUserInputParameter extends UserInputParameter{

    /**
     * 
     */
    private static final long serialVersionUID = -4813272643189340144L;

    private List<UserInputParameter> nestedInputParameters = new ArrayList<UserInputParameter>();

    /**
     * @return the nestedInputParameters
     */
    public List<UserInputParameter> getNestedInputParameters() {
        return nestedInputParameters;
    }

    /**
     * @param nestedInputParameters the nestedInputParameters to set
     */
    public void setNestedInputParameters(List<UserInputParameter> nestedInputParameters) {
        this.nestedInputParameters = nestedInputParameters;
    }
    
}
