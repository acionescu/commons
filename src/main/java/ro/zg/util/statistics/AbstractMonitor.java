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
/**
 * Abstract {@link MonitorBehaviour} implementation 
 * 
 *
 */
public abstract class AbstractMonitor implements MonitorBehaviour{
	private boolean enabled = true;
	private long refreshInterval = 5000;
	private boolean autoRefresh = true;

	
	private float rps;
	private float minRps = Float.MAX_VALUE;
	private float maxRps = Float.MIN_VALUE;
	private long maxRpsTimestamp;
	private long minRpsTimestamp;
	private long prevCallCount;
	private long lastRefreshTimestamp = System.currentTimeMillis();
	
	public AbstractMonitor(){
	}
	
	/* (non-Javadoc)
	 * @see com.cosmote.monitoring.MonitorBehaviour#isEnabled()
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/* (non-Javadoc)
	 * @see com.cosmote.monitoring.MonitorBehaviour#setEnabled(boolean)
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/* (non-Javadoc)
	 * @see com.cosmote.monitoring.MonitorBehaviour#getRefreshInterval()
	 */
	public long getRefreshInterval() {
		return refreshInterval;
	}

	/* (non-Javadoc)
	 * @see com.cosmote.monitoring.MonitorBehaviour#setRefreshInterval(long)
	 */
	public void setRefreshInterval(long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}
	
	/* (non-Javadoc)
	 * @see com.cosmote.monitoring.MonitorBehaviour#getAutoRefresh()
	 */
	public boolean getAutoRefresh() {
		return autoRefresh;
	}

	/* (non-Javadoc)
	 * @see com.cosmote.monitoring.MonitorBehaviour#setAutoRefresh(boolean)
	 */
	public void setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
	}
	
	/* (non-Javadoc)
	 * @see com.cosmote.monitoring.MonitorBehaviour#getLastRefreshTimestamp()
	 */
	public long getLastRefreshTimestamp() {
		return lastRefreshTimestamp;
	}

	/**
	 * Calculates the current , minimum and maximum number of requests/second
	 *
	 */
	private void calculateRps(){
		long callCount = getCallCount();
		long now = System.currentTimeMillis();
		rps = ( ( float )( ( callCount - prevCallCount ) ) * 1000 ) / ( now - lastRefreshTimestamp );
		prevCallCount = callCount;
		
		if(rps > 0 && rps < minRps ){
			minRps = rps;
			minRpsTimestamp = now;
		}
		
		if( rps > maxRps ){
			maxRps = rps;
			maxRpsTimestamp = now;
		}
	}
	/**
	 * Method called to refresh the data
	 * This method will be called on intervals defined by {@link #refreshInterval}
	 * In order to enhance the behaviour of refresh algorithm the subclasses 
	 * should implement the {@link #beforeRefresh()} and/or {@link #afterRefresh()} methods
	 * which by default are empty
	 * 
	 */
	public void refresh(){
		long timeSinceLastRefresh = System.currentTimeMillis() - getLastRefreshTimestamp();
		if(timeSinceLastRefresh < refreshInterval) {
			return;
		}
		beforeRefresh();
		calculateRps();
		afterRefresh();
		lastRefreshTimestamp = System.currentTimeMillis();
	}
	
	protected void beforeRefresh() {};
	
	protected void afterRefresh() {};
	
	/* (non-Javadoc)
	 * @see com.cosmote.monitoring.MonitorBehaviour#getAverageRps()
	 */
	public float getAverageRps() {
		return ( ( float ) ( getCallCount() * 1000 ) ) / ( System.currentTimeMillis() - getStartTimestamp() );
	}
	/* (non-Javadoc)
	 * @see com.cosmote.monitoring.MonitorBehaviour#getMaxRps()
	 */
	public float getMaxRps() {
		if(getAutoRefresh()){
			return maxRps;
		}
		return 0f;
	}
	/* (non-Javadoc)
	 * @see com.cosmote.monitoring.MonitorBehaviour#getMinRps()
	 */
	public float getMinRps() {
		if(getAutoRefresh()){
			return minRps;
		}
		return 0f;
	}
	/* (non-Javadoc)
	 * @see com.cosmote.monitoring.MonitorBehaviour#getRps()
	 */
	public float getRps() {
		if(getAutoRefresh()){
			return rps;
		}
		return 0f;
	}
	
	public long getMaxRpsTimestamp() {
	    return maxRpsTimestamp;
	}
	
	public long getMinRpsTimestamp() {
	    return minRpsTimestamp;
	}
	
}
