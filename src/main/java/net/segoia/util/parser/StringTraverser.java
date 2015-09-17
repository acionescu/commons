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
package net.segoia.util.parser;

import java.util.ArrayDeque;
import java.util.Deque;

public class StringTraverser {
    private String source;
    private int offset = 0;
    private int length = 0;
    private int checkPoint = 0;
    private Deque<Integer> skipPositions = new ArrayDeque<Integer>();

    public StringTraverser(String s) {
	this.source = s;
	this.length = s.length();

    }

    public String peek(int charsAhead) {
	int end = offset + charsAhead;
	if (end > length) {
	    throw new IllegalArgumentException(offset + "+" + charsAhead + " >= " + length);
	}
	return source.substring(offset, end);
    }

    public String read(int count) {
	String res = peek(count);
	offset += count;
	return res;
    }

    public void move(int count) {
	offset += count;
    }

    public void moveCheckPoint(int count) {
	checkPoint += count;
    }

    public String readFromCheckPoint(int count) {
	int end = checkPoint + count;
	if (end > length) {
	    throw new IllegalArgumentException(checkPoint + "+" + count + " >= " + length);
	}

	if(skipPositions.size() == 0) {
	    return source.substring(checkPoint,end);
	}
	
	int cp = checkPoint;
	StringBuffer result = new StringBuffer();

	while (skipPositions.size() > 0) {

	    int np = skipPositions.peek();
	    if (np >= end) {
		break;
	    }
	    result.append(source.substring(cp, np));
	    cp=skipPositions.poll()+1;
	}

	result.append(source.substring(cp, end));
	return result.toString();
    }

    /**
     * @return the checkPoint
     */
    public int getCheckPoint() {
	return checkPoint;
    }

    public int remained() {
	return length - offset;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
	return offset;
    }

    /**
     * @param checkPoint
     *            the checkPoint to set
     */
    public void setCheckPoint(int checkPoint) {
	this.checkPoint = checkPoint;
    }

    public boolean exhausted() {
	return offset >= (length);
    }
    
    public void addSkipPos(int pos) {
	skipPositions.add(pos);
    }

}
