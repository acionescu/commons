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

import java.sql.Timestamp;
import java.util.List;

public class SnapshotStats {
    private String monitorName;
    private Timestamp timestamp;
    
    private float averageExecutionTime;
    private long lastExecutionTime;
    private Timestamp lastExecutionTimestamp;
    private long maxExecutionTime;
    private Timestamp maxExecutionTimestamp;
    private long minExecutionTime;
    private Timestamp minExecutionTimestamp;
    private long pendingCallsCount;
    private long totalExecutionTime;
    private float averageRps;
    private float maxRps;
    private float minRps;
    private Timestamp maxRpsTimestamp;
    private Timestamp minRpsTimestamp;
    private long callCount;
    
    private List<SnapshotStats> childMonitorStats;
    
    
    
}
