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
 * $Id: Monitor.java,v 1.2 2007/11/13 13:39:15 aionescu Exp $
 */
package ro.zg.util.statistics;

import java.io.PrintStream;
import java.util.Date;

/**
 * @author Adrian Ionescu
 * @version $Revision: 1.2 $
 * 
 */
public class Monitor extends AbstractMonitor {
    private Counter counter;

    /**
     * The factory used to construct {@link Counter} objects
     */
    private CounterFactory counterFactory = new ParallelCounterFactory();

    // static fields
    private static MonitorManager monitorManager = new MonitorManager();

    // public access

    /**
     * Static method for getting a Monitor instance for the specified name
     */
    public static synchronized Monitor getMonitor(String name) {
	return monitorManager.getMonitor(name);
    }

    public static synchronized AggregatedMonitor getAggregatedMonitor(String name) {
	return monitorManager.getAggregatedMonitor(name);
    }

    /**
     * 
     * @param clazz
     *            - class object
     * @return a Monitor instance for the specified class
     */
    public static synchronized Monitor getMonitor(Class clazz) {
	return getMonitor(clazz.getName());
    }

    public static synchronized Monitor getMonitor(String monitorName, String aggregatedMonitorName) {
	return monitorManager.getMonitor(monitorName, aggregatedMonitorName);
    }

    /**
     * 
     * @return The names of all the monitors
     */
    public static String[] getMonitors() {
	return monitorManager.getMonitors();
    }

    public static String[] getAggregatedMonitors() {
	return monitorManager.getAggregatedMonitors();
    }

    /**
     * 
     * @return - The name of this monitor
     */
    public String getName() {
	return counter.getName();
    }

    /**
     * Starts a counter with the specified name If one doesn't exist it creates one
     * 
     * @param instructionName
     *            - the name of the counter to be started
     */
    public long startCounter() {
	if (isEnabled()) {
	    return counter.start();
	}
	throw new RuntimeException("Monitor '"+getName()+"' not enabled.");
    }

    /**
     * Stops the counter with the specified name
     * 
     * @param instructionName
     */
    public void stopCounter(long counterId) {
	if (isEnabled()) {
	    counter.stop(counterId);
	}
    }

    /**
     * Constructor
     * 
     * @param name
     *            - The name of the monitor
     */
    protected Monitor(String name) {
	if (name == null || "".equals(name)) {
	    throw new IllegalArgumentException("The name of the monitor cannot be null");
	}
	this.counter = counterFactory.createCounter(name);
    }

    public float getAverageExecutionTime() {
	return counter.getAverageExecutionTime();
    }

    public long getCallCount() {
	return counter.getCallCount();
    }

    public long getLastExecutionTimestamp() {
	return counter.getLastExecutionTimestamp();
    }

    public long getLastExecutionTime() {
	return counter.getLastExecutionTime();
    }

    public long getMaxExecutionTime() {
	return counter.getMaxExecutionTime();
    }

    public long getMinExecutionTime() {
	return counter.getMinExecutionTime();
    }

    public long getStartTimestamp() {
	return counter.getStartTimestamp();
    }

    public long getTotalExecutionTime() {
	return counter.getTotalExecutionTime();
    }

    public long getPendingCallsCount() {
	return counter.getPendingCallsCount();
    }

    public long getMaxExecutionTimestamp() {
	return counter.getMaxExecutionTimestamp();
    }

    public long getMinExecutionTimestamp() {
	return counter.getMinExecutionTimestamp();
    }
    
    public void printStats(PrintStream ps) {
	StringBuffer sb = new StringBuffer();
	sb.append("\nStarted at: "+new Date(getStartTimestamp()));
	sb.append("\nLast refresh: "+new Date(getLastRefreshTimestamp()));
	sb.append("\nRps: "+getRps());
	sb.append("\nLast execution: "+getLastExecutionTime()+" at "+new Date(getLastExecutionTimestamp()));
	sb.append("\nPending calls: "+getPendingCallsCount());
	sb.append("\nCalls: "+getCallCount());
	sb.append("\nAvg rps: "+getAverageRps());
	sb.append("\nAvg execution: "+getAverageExecutionTime());
	sb.append("\nMax rps: "+getMaxRps()+" at "+new Date(getMaxRpsTimestamp()));
	sb.append("\nMax execution: "+getMaxExecutionTime()+" at "+new Date(getMaxExecutionTimestamp()));
	sb.append("\nMin rps: "+getMinRps()+" at "+new Date(getMinRpsTimestamp()));
	sb.append("\nMin execution: "+getMinExecutionTime()+" at "+new Date(getMinExecutionTimestamp()));
	sb.append("\nTotal execution time: "+getTotalExecutionTime());
	sb.append("\nElapsed time: "+(System.currentTimeMillis()-getStartTimestamp()));
	ps.print(sb.toString());
    }
}
