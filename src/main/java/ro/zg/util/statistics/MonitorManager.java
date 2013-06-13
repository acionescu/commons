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
/**
 * $Id: MonitorManager.java,v 1.1 2007/11/13 13:39:15 aionescu Exp $
 */
package ro.zg.util.statistics;

import java.util.Hashtable;


public class MonitorManager {
	/**
	 * stores all the root monitors
	 */
	private Hashtable<String,Monitor> monitors = new Hashtable<String,Monitor>();

	
	/**
	 * Stores all the aggregated counters
	 */
	private Hashtable<String,AggregatedMonitor> aggregatedMonitors = new Hashtable<String,AggregatedMonitor>();
	
	/**
	 * Specifies if autorefresh is on or off
	 */
	private boolean autoRefresh = true;
	
	private long autoRefreshInterval = 1000; //that's ms
	
	private Thread autoRefreshThread;
	
	public MonitorManager() {
		startAutoRefresh();
	}
	
	//public access
	
	public synchronized Monitor getMonitor(String name) {
		if (monitors.containsKey(name)) {// checks if a monitor with this name already exists
			return (Monitor) monitors.get(name);
		}
		else {// creates a new monitor for the specified name
			Monitor monitor = createMonitor(name);
			monitors.put(name, monitor);
			return monitor;
		}
	}
	
	public synchronized AggregatedMonitor getAggregatedMonitor(String name){
		if (aggregatedMonitors.containsKey(name)) {// checks if a monitor with this name already exists
			return (AggregatedMonitor) aggregatedMonitors.get(name);
		}
		else {// creates a new monitor for the specified name
			AggregatedMonitor monitor = createAggregatedMonitor(name);
			aggregatedMonitors.put(name, monitor);
			return monitor;
		}
	}
	
	public synchronized Monitor getMonitor(String monitorName,String aggregatedMonitorName) {
		AggregatedMonitor aggregatedMonitor = getAggregatedMonitor(aggregatedMonitorName);
		Monitor monitor = getMonitor(monitorName);
		if(monitor == null) {
			monitor = createMonitor(monitorName);
			return monitor;
		}
		
		if(!aggregatedMonitor.containsMonitor(monitor)){
			aggregatedMonitor.addChildMonitor(monitor);
		}
		return monitor;
	}
	
	/**
	 * 
	 * @return The names of all the monitors
	 */
	public String[] getMonitors() {
		return (String[])monitors.keySet().toArray(new String[0]);
	}	
	
	public String[] getAggregatedMonitors(){
		return (String[])aggregatedMonitors.keySet().toArray(new String[0]);
	}
	
	public boolean getAutoRefresh() {
		return autoRefresh;
	}

	public void setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
		if(autoRefresh){
			startAutoRefresh();
		}
	}

	public long getAutoRefreshInterval() {
		return autoRefreshInterval;
	}

	public void setAutoRefreshInterval(long autoRefreshInterval) {
		this.autoRefreshInterval = autoRefreshInterval;
	}
	
//private access
	


	private  Monitor createMonitor(String name) {
		Monitor monitor = new Monitor(name);
		MonitorConfigurationBuilder.configure(monitor);
		return monitor;
	}
	
	private AggregatedMonitor createAggregatedMonitor(String name){
		AggregatedMonitor monitor = new AggregatedMonitor(name);
		MonitorConfigurationBuilder.configure(monitor);
		return monitor;
	}
	
	//refresh mechanism
	/**
	 * Starts autorefresh mechanism
	 * This method is called when {@link #autoRefresh} is turned on
	 *
	 */
	private void startAutoRefresh(){
		if(autoRefreshThread == null || !autoRefreshThread.isAlive()){
			if(getAutoRefresh()){
				autoRefreshThread = new Thread(new AutoRefreshJob());
				autoRefreshThread.setDaemon(true);
				autoRefreshThread.start();
			}
		}
	}
		
	/**
	 * Private class which calles the refresh method on this monitor
	 *
	 */
	private class AutoRefreshJob implements Runnable{
		
		public void run() {//calles refresh method on monitor instance if autorefresh is turned on
			while(getAutoRefresh()) {
				//refresh aggregated counters
				for(MonitorBehaviour monitor : aggregatedMonitors.values()) {
					refreshMonitor(monitor);
				}
				// refresh simple monitors
				for(MonitorBehaviour monitor : monitors.values()) {
					refreshMonitor(monitor);
				}
				
				try {
					Thread.sleep(getAutoRefreshInterval());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		private void refreshMonitor(MonitorBehaviour monitor) {
			long refreshInterval = monitor.getRefreshInterval();
			if(refreshInterval > 0 && monitor.getAutoRefresh()) {
				monitor.refresh();
			}
			
		}
	}
	
}
