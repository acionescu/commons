/**
 * commons - Various Java Utils
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.util.statistics;

public interface CounterBehaviour {
	/**
	 * 
	 * @return The number of times the counter has been called
	 */
	public long getCallCount();
	
	/**
	 * 
	 * @return
	 */
	public long getPendingCallsCount();
	/**
	 * 
	 * @return The last measured time
	 */
	public long getLastExecutionTime();
	/**
	 * 
	 * @return The sum of all the measured times
	 */
	public long getTotalExecutionTime();
	/**
	 * 
	 * @return The average of the measured times
	 */
	public float getAverageExecutionTime();
	/**
	 * 
	 * @return The last time when the couter was called
	 */
	public long getLastExecutionTimestamp();
	/**
	 * 
	 * @return The time when this counter was instantiated
	 */
	public long getStartTimestamp();
	
	/**
	 * 
	 * @return Mininum execution time since the counter was started
	 */
	public long getMinExecutionTime();
	
	/**
	 * 
	 * @return Maximum execution time since the counter was started
	 */
	public long getMaxExecutionTime();	
	
	public long getMaxExecutionTimestamp();
	
	public long getMinExecutionTimestamp();
}
