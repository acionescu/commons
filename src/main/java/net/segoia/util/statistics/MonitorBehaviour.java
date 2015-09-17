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

public interface MonitorBehaviour extends CounterBehaviour{
	
	/**
	 * 
	 * @return Average number of requests/second
	 */
	public float getAverageRps();
	
	/**
	 * 
	 * @return Number of requests/second
	 */
	public float getRps();
	/**
	 * 
	 * @return The minimum value of request/second since the counter was started
	 */
	public float getMinRps();
	
	/**
	 * 
	 * @return The maximum value of request/second since the counter was started
	 */
	public float getMaxRps();
	
	/**
	 * 
	 * @return the name of the monitor
	 */
	public String getName();
	
	/**
	 * 
	 * @return True is this monitor is enabled, false otherwise
	 */
	public boolean isEnabled();
	
	/**
	 * @param enabled - true to enable this monitor false to desable it
	 */
	public void setEnabled(boolean enabled);
	
	/**
	 * 
	 * @param refreshInterval - The inverval in milliseconds in which the counter should autorefresh
	 */
	public void setRefreshInterval(long refreshInterval);
	/**
	 * 
	 * @return the interval in miliseconds used by the counter to autorefresh
	 */
	public long getRefreshInterval();
	
	/**
	 * 
	 * @return return true if autorefresh is on false otherwise
	 */
	public boolean getAutoRefresh();
	
	/**
	 * 
	 * @param autoRefresh - set autorefresh on or off
	 * Setting this on will start the autorefresh thread immediately
	 * Setting it off will stop the autorefresh thread which will be terminated 
	 * in less then the value specified by autoRefreshInterval variable
	 */
	public void setAutoRefresh(boolean autoRefresh);
	
	/**
	 * 
	 * @return Last timestamp  when the data was refreshed
	 */
	public long getLastRefreshTimestamp();
	
	/**
	 * Refreshes the date for this {@link MonitorBehaviour} implementation
	 *
	 */
	public void refresh();
	
	public long getMaxRpsTimestamp();
	
	public long getMinRpsTimestamp();

}
