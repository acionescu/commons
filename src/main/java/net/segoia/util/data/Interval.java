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
package net.segoia.util.data;

public class Interval {
    public static final char DELIMITER='-';
    
    private long start;
    private long end;
    /*
     * true if this interval represents the end of a sequence(eos), in which case the end can be deduced as the last
     * position in the sequence
     */
    private boolean eos;

    public Interval(long start, long end) {
	super();
	this.start = start;
	this.end = end;
    }

    public Interval(long start, long end, boolean eos) {
	super();
	this.start = start;
	this.end = end;
	this.eos = eos;
    }

    /**
     * @return the start
     */
    public long getStart() {
	return start;
    }

    /**
     * @return the end
     */
    public long getEnd() {
	return end;
    }

    public long getLength() {
	return end - start;
    }

    /**
     * @return the eos
     */
    public boolean isEos() {
	return eos;
    }
    
    public boolean isEmpty() {
	return start==0 && (start==end);
    }
    
    public boolean contains(Interval i) {
	return (start<=i.start && end >= i.end);
    }
    
    public boolean intersects(Interval i) {
	return (start < i.start && end >= i.start && end < i.end) 
	|| (start > i.start && start <= i.end && end > i.end);
    }
    
    public void trimBy(Interval i) {
	if(contains(i)) {
	    return;
	}
	if(start <= i.end) {
	    start=i.end+1;
	}
	else if(end >= i.start) {
	    end=i.start-1;
	}
    }
    
    public void trimTail(int count) {
	end-=count;
    }
    
    public void trimHead(int count) {
	start+=count;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	String result = "";
	if (start > 0) {
	    result += start;
	    if (!eos) {
		result += DELIMITER;
	    }
	}
	if (!eos && end > 0) {
	    result += end;
	}
	return result;
    }

}
