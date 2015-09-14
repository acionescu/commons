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
package ro.zg.util.data.encoding;

import ro.zg.util.data.Cell;
import ro.zg.util.data.Interval;

public class Encoder {
    public static final char START_MATCH_BLOCK = ':';
    public static final char START_DIFF_BLOCK = '+';
    public static final char ESCAPE_CHAR = '\\';

    private String current;
    private String origin;

    private String erased = "";
    private Interval prevMatch = null;
    private int maxMatchPos = -1;
    StringBuffer result = new StringBuffer();

    public Encoder(String current, String origin) {
	super();
	this.current = current;
	this.origin = origin;
    }

    public String encode() {
	if (origin == null && current == null) {
	    return result.toString();
	} else if (origin != null && current == null) {
	    result.append(origin);
	    return result.toString();
	} else if (origin == null && current != null) {
	    return result.toString();
	}

	Cell index = new Cell();
	index.indexMaximal(current);

	for (int i = 0; i < origin.length(); i++) {
	    String s = origin.substring(i);
	    Interval interval = index.findMatchPos(s);
	    if (interval.getLength() > interval.toString().length()) {
		if (prevMatch != null) {
		    if (prevMatch.contains(interval)) {
			continue;
		    }
		    int offset = maxMatchPos - i + 1;
		    prevMatch.trimTail(offset);
		    appendMatchToResult(prevMatch);
		}

		if ((interval.getLength() + 1) == (origin.length() - i)) {
		    appendMatchToResult(interval);
		    break;
		}
		maxMatchPos = i + (int) interval.getLength();
		prevMatch = interval;

	    } else if (i > maxMatchPos) {
		if (prevMatch != null) {
		    appendMatchToResult(prevMatch);
		    prevMatch = null;
		}
		appendDiffChar(origin.charAt(i));
	    }
	}
	if (!"".equals(erased)) {
	    appendDiffToResult(erased);
	}

	return result.toString();
    }

    private void appendDiffToResult(String diff) {
	if (result.length() > 0) {
	    result.append(START_DIFF_BLOCK);
	}
	result.append(diff);
    }

    private void appendMatchToResult(Interval match) {
	if (match.getLength() < match.toString().length()) {
	    erased += current.substring((int) match.getStart(), (int) match.getEnd() + 1);
	} else {
	    if (!"".equals(erased)) {
		appendDiffToResult(erased);
		erased = "";
	    }
	    
	    result.append(START_MATCH_BLOCK + match.toString());
	}
    }

    private void appendDiffChar(char c) {
	if (c == START_MATCH_BLOCK || c == ESCAPE_CHAR) {
	    erased += ESCAPE_CHAR;
	}
	erased += c;
    }

}
