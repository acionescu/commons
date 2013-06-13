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
package ro.zg.util.monitoring;

import ro.zg.commons.exceptions.ContextAwareException;
import ro.zg.util.logging.Logger;
import ro.zg.util.logging.MasterLogManager;
import ro.zg.util.statistics.Monitor;

public class DefaultMonitoringManager implements MonitoringManager {
    private LogPersister logPersister;
    private Logger localLogger = MasterLogManager.getLogger("MONITORING_MANAGER");

    public void addLogEvent(LogEvent logEvent) {
	try {
	    logPersister.log(logEvent);
	} catch (Exception e) {
	    localLogger.error("failed to persist log event " + logEvent, e);
	}
    }

    public long startMonitoring(String monitoringKey) {
	Monitor m = Monitor.getMonitor(monitoringKey);
	return m.startCounter();
    }

    public void stopMonitoring(String monitoringKey, long counterId) {
	Monitor m = Monitor.getMonitor(monitoringKey);
	m.stopCounter(counterId);
    }

    public void stopMonitoring(String monitoringKey, Long counterId) {
	stopMonitoring(monitoringKey, counterId.longValue());
    }

    public long startMonitoring(String monitoringKey, String masterMonitorKey) throws ContextAwareException {
	Monitor m = Monitor.getMonitor(monitoringKey, masterMonitorKey);
	return m.startCounter();
    }
}
