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
package net.segoia.util.data.encoding;

import net.segoia.util.data.Interval;

public class Decoder {
    private String target;
    private String reference;

    StringBuffer result = new StringBuffer();
    int start = 0;
    int end = 0;
    String val = "";
    boolean matchBlock = false;
    boolean escaped = false;

    public Decoder(String target, String reference) {
	super();
	this.target = target;
	this.reference = reference;
    }

    public String decode() {
	if(target.equals(""+Encoder.START_MATCH_BLOCK)) {
	    return reference;
	}
	for (int i = 0; i < target.length(); i++) {
	    char c = target.charAt(i);

	    switch (c) {
	    case Encoder.ESCAPE_CHAR:
		if (!escaped) {
		    escaped = true;
		    continue;
		}
		
	    case Encoder.START_MATCH_BLOCK:
		if (!escaped) {
		    processBlockStart();
		    matchBlock = true;
		    break;
		}
	    case Encoder.START_DIFF_BLOCK:
		if (matchBlock) {
		    processBlockStart();
		    matchBlock = false;
		    continue;
		}
	    default:
		if (escaped) {
		    escaped = false;
		}
		if (matchBlock) {
		    if (c == Interval.DELIMITER) {
			start = Integer.parseInt(val);
			val = "";
		    } else {
			val += c;
		    }
		} else {
		    result.append(c);
		}
	    }
	}

	if (!"".equals(val)) {
	    result.append(reference.substring(Integer.parseInt(val)));
	}

	return result.toString();
    }

    private void processBlockStart() {
	if (!"".equals(val)) {
	    end = Integer.parseInt(val);
	    val = "";
	    result.append(reference.subSequence(start, end + 1));
	}
    }
}
