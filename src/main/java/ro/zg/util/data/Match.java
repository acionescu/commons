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

public class Match {
    private long position;
    private Interval matchInterval;
    
    public Match(long position, Interval matchInterval) {
	super();
	this.position = position;
	this.matchInterval = matchInterval;
    }

    /**
     * @return the position
     */
    public long getPosition() {
        return position;
    }

    /**
     * @return the matchInterval
     */
    public Interval getMatchInterval() {
        return matchInterval;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return position+":"+matchInterval.toString();
    }
    
    
}
