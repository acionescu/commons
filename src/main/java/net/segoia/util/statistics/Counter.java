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

public abstract class Counter implements CounterBehaviour{
	private String name;
	private long startTimestamp;
	/**
	 * Constructor 
	 * @param name - name of the counter
	 */
	public Counter(String name) {
		if( name == null || "".equals(name)) {
			throw new IllegalArgumentException("Name of the counter cannot be null");
		}
		this.name=name;
		this.startTimestamp = System.currentTimeMillis();
	}
	
	/**
	 * @return the name of this counter
	 */
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see com.cosmote.monitoring.CounterBehaviour#getStartTimestamp()
	 */
	public long getStartTimestamp() {
		return startTimestamp;
	}
	/**
	 * Start the counter
	 *
	 */
	public abstract long start();
	/**
	 * Stop the counter
	 * The counter can only be stopped by the thread who started it
	 */
	public abstract void stop(long counterId);
	/**
	 * Reset the counter
	 *
	 */
	public abstract void reset();

}
