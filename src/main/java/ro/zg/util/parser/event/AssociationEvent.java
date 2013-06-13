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
package ro.zg.util.parser.event;

public class AssociationEvent extends AbstractParseEvent{
    private Object prefixValue;
    private Object postfixValue;
    /**
     * @return the prefixValue
     */
    public Object getPrefixValue() {
        return prefixValue;
    }
    /**
     * @return the postfixValue
     */
    public Object getPostfixValue() {
        return postfixValue;
    }
    /**
     * @param prefixValue the prefixValue to set
     */
    public void setPrefixValue(Object prefixValue) {
        this.prefixValue = prefixValue;
    }
    /**
     * @param postfixValue the postfixValue to set
     */
    public void setPostfixValue(Object postfixValue) {
        this.postfixValue = postfixValue;
    }
    
    
}
