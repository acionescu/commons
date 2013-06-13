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
package ro.zg.util.statistics;

import java.util.Hashtable;
import java.util.Iterator;

public class AggregatedMonitor extends AbstractMonitor{

	private long lastExecutionTime;

	private long totalExecutionTime;
	
	private float averageExecutionTime;

	private long callCount;
	
	private long pendingCallsCount;
	
	private long minExecutionTime = Long.MAX_VALUE;
	
	private long maxExecutionTime = Long.MIN_VALUE;
	
	private long minExecutionTimestamp;
	
	private long maxExecutionTimestamp;
	
	/**
	 * Stores the child ExtendedMonitorBehaviour instances
	 */
	private Hashtable<String,MonitorBehaviour> childMonitors= new Hashtable<String,MonitorBehaviour>();
	
	private long lastExecutionTimestamp;
	
	private long startTimestamp;
	
	private String name;
	
	public AggregatedMonitor(String name){
		if( name == null || "".equals(name)) {
			throw new IllegalArgumentException("Name of the counter cannot be null");
		}
		this.name=name;
		this.startTimestamp = System.currentTimeMillis();
	}
	protected void beforeRefresh() {
		aggregate();
	}
	
	/**
	 *Aggregates all the data from the child {@link MonitorBehaviour} instances
	 *
	 */
	private void aggregate() {
		if(childMonitors.size() <=0 ){
			return;
		}
		long cc=0; //local variable to calculate callCount
		long tpt=0;//local variable to calculate totalProcessTime
		long ucc=0;//local variable to calculate unfinished calls
		Iterator it = childMonitors.keySet().iterator();
		while(it.hasNext()) {
			MonitorBehaviour m =(MonitorBehaviour)childMonitors.get(it.next());
			//get most recent date
			if(m.getLastExecutionTimestamp() > lastExecutionTimestamp) {//get the most recent process time
				lastExecutionTimestamp = m.getLastExecutionTimestamp();
				lastExecutionTime = m.getLastExecutionTime();
			}
			//get min execution time
			if(m.getMinExecutionTime() < minExecutionTime){
				minExecutionTime = m.getMinExecutionTime();
				minExecutionTimestamp = m.getMinExecutionTimestamp();
			}
			//get max execution time
			if(m.getMaxExecutionTime() > maxExecutionTime){
				maxExecutionTime = m.getMaxExecutionTime();
				maxExecutionTimestamp = m.getMaxExecutionTimestamp();
			}
			
			cc+=m.getCallCount();
			tpt+=m.getTotalExecutionTime();
			ucc+=m.getPendingCallsCount();
		}
		callCount = cc;
		pendingCallsCount = ucc;
		totalExecutionTime = tpt;
		averageExecutionTime = (callCount != 0)?(float)totalExecutionTime/callCount:0;
	}
		
	
	/* (non-Javadoc)
	 * @see com.cosmote.rtbus.monitoring.CounterMonitor#getLastProcessTime()
	 */
	public long getLastExecutionTime() {
		return lastExecutionTime;
	}
	
	/* (non-Javadoc)
	 * @see com.cosmote.rtbus.monitoring.CounterMonitor#getTotalProcessTime()
	 */
	public long getTotalExecutionTime() {
		return totalExecutionTime;
	}
	
	/* (non-Javadoc)
	 * @see com.cosmote.rtbus.monitoring.CounterMonitor#getAverageProcessTime()
	 */
	public float getAverageExecutionTime() {
		return averageExecutionTime;
	}
	
	/* (non-Javadoc)
	 * @see com.cosmote.rtbus.monitoring.CounterMonitor#getCallCount()
	 */
	public long getCallCount() {
		return callCount;
	}
	
	/* (non-Javadoc)
	 * @see com.cosmote.rtbus.monitoring.CounterMonitor#getLastExecutionTimestamp()
	 */
	public long getLastExecutionTimestamp() {
		return lastExecutionTimestamp;
	}
	
	public void addChildMonitor(MonitorBehaviour m) {
		childMonitors.put(m.toString(),m);
	}
	
	public void removeChildCounterMonitor(MonitorBehaviour m) {
		childMonitors.remove(m.toString());
	}
	
	public int getChildrenCount() {
		return childMonitors.size();
	}
	
	public boolean containsMonitor(MonitorBehaviour m){
		return childMonitors.containsKey(m.toString());
	}
	
	public long getMaxExecutionTime() {
		return maxExecutionTime;
	}

	public long getMinExecutionTime() {
		return minExecutionTime;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public String getName() {
		return name;
	}
	public long getPendingCallsCount() {
	  return pendingCallsCount;
	}
	public long getMaxExecutionTimestamp() {
	    return maxExecutionTimestamp;
	}
	public long getMinExecutionTimestamp() {
	    return minExecutionTimestamp;
	}

}
