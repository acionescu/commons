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

import org.apache.log4j.Logger;

/**
 * Thread safe Counter implementation which doesn't block until the thread that called {@link #startCounter()} calls
 * {@link #stopCounter()} .
 * 
 * The behavior is like this:
 * 
 * When a new thread calls {@link #startCounter()} the current time is stored in the threadsStartTimes map keeping the
 * id of the thread as key.
 * 
 * And when a thread calls {@link #stopCounter()} the lastProcessTime is calculated using the stored value in the
 * threadStartTimes map for this thread
 * 
 * @author aionescu
 * @version $Revision: 1.1 $
 */
public class ParallelCounter extends Counter {
    private static Logger logger = Logger.getLogger(ParallelCounter.class);

    /**
     * Stores the last process time
     */
    private long lastExecutionTime;

    /**
     * Stores the sum of all the measured process times Attention : The value of this variable will not neccesarily be
     * equal with the actual proccess time especially in multithreaded environments This is only used to calculate the
     * average process time
     */
    private long totalExecutionTime;

    /**
     * Stores the number o times the {@link #startCounter()} method was called
     */
    private long callCount;

    private long nextCounterId;

    /**
     * Stores the last timestamp when a thread called the {@link #startCounter()} method key - counterId
     */
    private Hashtable<Long, Long> startTimes = new Hashtable<Long, Long>();

    /**
     * Stores the last timestamp when {@link #startCounter()} was called
     */
    private long lastExecutionTimestamp;
    /**
     * Stores minimum execution time
     */
    private long minExecutionTime = Long.MAX_VALUE;
    /**
     * Stores maximum execution time
     */
    private long maxExecutionTime = Long.MIN_VALUE;
    
    private long minExecutionTimestamp;
    
    private long maxExecutionTimestamp;

    /**
     * 
     * @param name
     *            - The name of the Counter
     */
    public ParallelCounter(String name) {
	super(name);
	// TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.monitoring.Counter#startCounter()
     */
    public long start() {
	long counterId = nextCounterId++;
	if (startTimes.containsKey(counterId)) {
	    logger.error("The counter " + getName() + " with id " + counterId + " is already started.");
	}
	startTimes.put(counterId, new Long(System.currentTimeMillis()));
	callCount++;
	return counterId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.monitoring.Counter#stopCounter()
     */
    public void stop(long counterId) {
	Long startTimeLong = (Long) startTimes.remove(counterId);
	if (startTimeLong == null) {
	    logger.error("The couner " + getName() + " was not started on thread " + Thread.currentThread().getId());
	} else {
	    long startTime = startTimeLong.longValue();
	    long lastTime = System.currentTimeMillis() - startTime;
	    totalExecutionTime += lastTime;
	    lastExecutionTime = lastTime;
	    lastExecutionTimestamp = System.currentTimeMillis();
	    // update minimum execution time
	    if (lastTime < minExecutionTime) {
		minExecutionTime = lastTime;
		minExecutionTimestamp = lastExecutionTimestamp;
		
	    }
	    // update maximum execution time
	    if (lastTime > maxExecutionTime) {
		maxExecutionTime = lastTime;
		maxExecutionTimestamp = lastExecutionTimestamp;
	    }

	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.monitoring.CounterMonitor#getCallCount()
     */
    public long getCallCount() {
	return callCount;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.monitoring.CounterMonitor#getLastProcessTime()
     */
    public long getLastExecutionTime() {
	return lastExecutionTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.monitoring.Counter#isStarted()
     */
    public boolean isStarted() {
	return startTimes.contains(Thread.currentThread().getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.monitoring.CounterMonitor#getTotalProcessTime()
     */
    public long getTotalExecutionTime() {
	return totalExecutionTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.monitoring.CounterMonitor#getAverageProcessTime()
     */
    public float getAverageExecutionTime() {
	return (callCount != 0) ? (float)totalExecutionTime / callCount : 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.monitoring.CounterMonitor#getLastExecutionTimestamp()
     */
    public long getLastExecutionTimestamp() {
	return lastExecutionTimestamp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.monitoring.Counter#reset()
     */
    public void reset() {
	lastExecutionTime = 0;
	callCount = 0;
	totalExecutionTime = 0;
	lastExecutionTimestamp = 0;
	minExecutionTime = 0;
	maxExecutionTime = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.monitoring.CounterMonitor#getAverageRps()
     */
    public float getAverageRps() {
	return callCount * 1000 / (System.currentTimeMillis() - getStartTimestamp());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.monitoring.CounterMonitor#getMaxExecutionTime()
     */
    public long getMaxExecutionTime() {
	return maxExecutionTime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cosmote.rtbus.monitoring.CounterMonitor#getMinExecutionTime()
     */
    public long getMinExecutionTime() {
	return minExecutionTime;
    }

    public long getPendingCallsCount() {
	return startTimes.size();
    }

    public long getMaxExecutionTimestamp() {
	return maxExecutionTimestamp;
    }

    public long getMinExecutionTimestamp() {
	return minExecutionTimestamp;
    }
}
