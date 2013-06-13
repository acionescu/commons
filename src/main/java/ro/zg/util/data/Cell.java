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
package ro.zg.util.data;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;


public class Cell {
    protected char data;
    protected Cell value;
    protected Cell ref;

    protected Cell lastRef;
    protected long position;

    private static final char POP_VALUE = 'a';
    private static final char PUSH_VALUE = 'b';
    private static final char POP_REF = 'c';
    private static final char PUSH_REF = 'd';
    private static final char COPY_TO_VALUE = 'e';
    private static final char COPY_FROM_VALUE = 'f';
    private static final char COPY_TO_REF = 'g';
    private static final char COPY_FROM_REF = 'h';
    private static final char SWITCH = 'i';
    private static final char VALUE_EQ_REF = 'j';
    private static final char REF_EQ_VALUE = 'k';
    private static final char PUSH_VALUE_TO_REF = 'l';
    private static final char PUSH_REF_TO_VALUE = 'm';
    private static final char DELETE_VALUE = 'n';
    private static final char DELETE_REF = 'o';
    private static final char SELECT_VALUE = 'p';
    private static final char SELECT_REF = 'r';

    public static final char[] operations = new char[] { POP_VALUE, PUSH_VALUE, SELECT_VALUE, SELECT_REF, POP_REF,
	    PUSH_REF, COPY_TO_VALUE, COPY_FROM_VALUE, COPY_TO_REF, COPY_FROM_REF, SWITCH, VALUE_EQ_REF, REF_EQ_VALUE,
	    PUSH_VALUE_TO_REF, PUSH_REF_TO_VALUE, DELETE_VALUE, DELETE_REF };

    public Cell(char data, Cell ref) {
	super();
	this.data = data;
	this.ref = ref;
    }

    public Cell(char data, Cell value, Cell ref) {
	super();
	this.data = data;
	this.value = value;
	this.ref = ref;
    }

    public Cell(Cell value, Cell ref) {
	super();
	this.value = value;
	this.ref = ref;
    }

    public Cell(char data) {
	super();
	this.data = data;
    }

    public Cell(Cell value) {
	super();
	this.value = value;
    }

    public Cell() {
	super();
    }

    public Cell(String s) {
	if (s == null || s.isEmpty()) {
	    return;
	}
	Cell last = null;
	for (int i = s.length() - 1; i >= 0; i--) {
	    Cell head = new Cell(s.charAt(i), last);
	    last = head;
	}
	this.data = last.data;
	this.value = last.value;
	this.ref = last.ref;
    }

    private Cell copy() {
	return new Cell(data, value, ref);
    }

    private char getData() {
	if (data != (char) 0) {
	    return data;
	} else if (value != null) {
	    return value.getData();
	}
	return data;
    }

    private Cell comp(Cell c) {

	char data1 = getData();
	char data2 = c.getData();

	boolean negative = false;
	if (data1 == '-' && data2 != '-') {
	    return new Cell('2');
	} else if (data1 != '-' && data2 == '-') {
	    return new Cell('1');
	} else if (data1 == '-' && data2 == '-') {
	    negative = true;
	}

	char comp = comp(data1, data2);

	Cell link1 = ref;
	Cell link2 = c.ref;

	while (link1 != null || link2 != null) {
	    if (link1 != null && link2 == null) {
		comp = '1';
		break;
	    } else if (link1 == null && link2 != null) {
		comp = '2';
		break;
	    } else if (link1 != null && link2 != null) {
		if (comp == '0') {
		    comp = comp(link1.getData(), link2.getData());
		}
		link1 = link1.ref;
		link2 = link2.ref;
	    }
	}
	if (negative && comp != '0') {
	    if (comp == '1') {
		comp = '2';
	    } else {
		comp = '1';
	    }
	}
	return new Cell(comp);
    }

    private char comp(char c1, char c2) {
	if (c1 > c2) {
	    return '1';
	} else if (c1 < c2) {
	    return '2';
	}
	return '0';
    }

    public Cell getRef(char c, boolean create) {
	Cell next = ref;
	Cell prev = null;
	while (next != null) {
	    if (next.value != null && next.value.data == c) {
		/* bring the found cell to front */
		if (prev != null) {
		    Cell followers = next.ref;
		    next.ref = ref;
		    ref = next;
		    prev.ref = followers;
		}
		return next.value;
	    }
	    prev = next;
	    next = next.ref;
	}
	if (create) {
	    Cell newVal = new Cell(c);

	    // ref = new Cell(newVal, ref);
	    pushRef(newVal);
	    // addRef(newVal);
	    return newVal;
	}
	return null;
    }

    public Cell getValue(char c) {
	if (data == c) {
	    return this;
	}
	Cell next = value;
	while (next != null) {
	    if (next.value != null && next.value.data == c) {
		return next.value;
	    }
	    next = next.ref;
	}
	return null;
    }

    public void pushValue(Cell c) {
	// Cell next=value;
	// while(next != null) {
	// if (next.value != null && next.value.data==c.data) {
	// return;
	// }
	// next=next.ref;
	// }
	value = new Cell(c, value);
    }

    public void pushRef(Cell c) {
	ref = new Cell(c, ref);
	if (lastRef == null) {
	    lastRef = ref;
	}
    }

    public void addRef(Cell c) {
	if (lastRef == null) {
	    ref = new Cell(c);
	    lastRef = ref;
	} else {
	    lastRef.ref = new Cell(c);
	    lastRef = lastRef.ref;
	}
    }

    public Cell index(String s) {
	if (s == null) {
	    return this;
	}
	Cell prev = this;
	for (int i = 0; i < s.length(); i++) {
	    char c = s.charAt(i);
	    Cell current = null;
	    if (i > 0) {
		current = prev.getRef(c, true);
	    } else {
		current = new Cell(c);
	    }
	    current.position = i;
	    // current.pushValue(prev);

	    // if (i > 0) {
	    // this.getRef(prev.data, true).pushRef(current);
	    // this.getRef(c, true).pushRef(current);
	    this.getRef(c, true).addRef(current);
	    // }

	    prev = current;
	}
	return prev;
    }

    public void digest(String s) {
	if (s == null) {
	    return;
	}
	for (int k = 0; k < (s.length() - 1); k++) {
	    Cell prev = this;
	    for (int i = k; i < s.length(); i++) {
		char c = s.charAt(i);
		Cell current = prev.getRef(c, true);
		prev = current;
	    }
	}

	for (int k = (s.length() - 1); k > 0; k--) {
	    Cell prev = this;
	    for (int i = k; i >= 0; i--) {
		char c = s.charAt(i);
		Cell current = prev.getRef(c, true);
		prev = current;
	    }
	}
    }

    public Cell search(String s) {
	if (s == null || s.isEmpty()) {
	    return null;
	}
	Cell result = new Cell();
	char firstChar = s.charAt(0);
	Cell next = this.getRef(firstChar, false);

	// if (next != null) {
	// next = next.ref;
	// }
	while (next != null) {

	    // Cell last = next.value;
	    Cell last = next;
	    // if (last == null) {
	    // continue;
	    // }
	    for (int i = 0; i < s.length(); i++) {
		char c = s.charAt(i);
		last = last.getRef(c, false);
		if (last == null) {
		    break;
		}
	    }
	    if (last != null && last.ref != null) {
		last = last.ref.value;
	    }
	    if (last != null) {
		result.pushRef(last);
	    }
	    next = next.ref;
	}
	if (result.ref != null) {
	    return result;
	}
	return null;
    }

    public Interval findMatchPos(String s) {
	long endPos = 0;
	long startPos = -1;
	/* true if the found interval is the last in the searched sequence */
	boolean eos = false;
	if (s == null || s.isEmpty()) {
	    return new Interval(startPos, endPos);
	}
	char firstChar = s.charAt(0);
	Cell next = this.getRef(firstChar, false);
	// if (next != null) {
	// next = next;
	// }
	while (next != null) {
	    Cell last = next;
	    long localStart = 0;
	    eos = false;
	    for (int i = 0; i < s.length(); i++) {
		char c = s.charAt(i);
		last = last.getRef(c, false);
		if (last == null) {
		    break;
		}
		if (i == 0) {
		    localStart = last.position;
		    if (startPos == -1) {
			startPos = localStart;
		    }
		}
		if (i > endPos) {
		    endPos = i;
		    startPos = localStart;
		}
	    }
	    if (last != null && last.ref == null) {
		eos = true;
	    }
	    if (endPos == (s.length() - 1)) {
		break;
	    }
	    // if (last != null && last.ref != null) {
	    // last = last.ref.value;
	    // }
	    next = next.ref;
	}
	return new Interval(startPos, startPos + endPos, eos);
    }

    public Cell operate(Cell target) {
	Cell v1 = null;
	Cell v2 = null;
	switch (data) {
	case POP_VALUE:
	    if (target.value != null) {
		target.data = target.value.data;
		target.value = target.value.value;
	    }
	    break;
	case PUSH_VALUE:
	    v1 = new Cell(target.data);
	    v1.value = target.value;
	    target.value = v1;
	    target.data = (char) 0;
	    break;
	case SELECT_VALUE:
	    return target.value;

	case SELECT_REF:
	    return target.ref;

	case POP_REF:
	    if (target.ref != null) {
		target.data = target.ref.data;
		target.ref = target.ref.ref;
	    }
	    break;
	case PUSH_REF:
	    v1 = new Cell(target.data);
	    v1.ref = target.ref;
	    target.ref = v1;
	    target.data = (char) 0;
	    break;

	case COPY_TO_VALUE:
	    v1 = new Cell(target.data);
	    v1.value = target.value;
	    target.value = v1;
	    break;

	case COPY_FROM_VALUE:
	    if (target.value != null) {
		target.data = target.value.data;
	    }
	    break;
	case COPY_TO_REF:
	    v1 = new Cell(target.data);
	    v1.ref = target.ref;
	    target.ref = v1;
	    break;
	case COPY_FROM_REF:
	    if (target.ref != null) {
		target.data = target.ref.data;
	    }
	    break;
	case SWITCH:
	    v1 = target.value;
	    target.value = target.ref;
	    target.ref = v1;
	    break;

	case VALUE_EQ_REF:
	    target.value = target.ref;
	    break;
	case REF_EQ_VALUE:
	    target.ref = target.value;
	    break;
	case PUSH_VALUE_TO_REF:
	    v1 = new Cell(target.value, target.ref);
	    target.value = null;
	    target.ref = v1;
	    break;
	case PUSH_REF_TO_VALUE:
	    v1 = new Cell(target.ref);
	    v1.value = target.value;
	    target.value = v1;
	    break;

	case DELETE_VALUE:
	    target.value = null;
	    break;
	case DELETE_REF:
	    target.ref = null;
	    break;
	}
	return target;
    }

    public Cell execute(Cell source) {
	if (source == null) {
	    source = new Cell();
	}
	Cell result = operate(source);
	if (value != null) {
	    result = value.execute(result);
	}
	if (ref != null) {
	    result = ref.execute(result);
	}
	if (result == null) {
	    return new Cell();
	}
	return result;
    }

    

    public String print(StringBuffer result, String offset, Set<Cell> processed) {

	String newLine = "\n";
	if (data != (char) 0) {
	    if (processed.contains(this)) {
		return result.toString();
	    }
	    processed.add(this);

	    result.append(data);
	}

	if (value != null) {
	    value.print(result, offset + " ", processed);
	}
	result.append(newLine);
	result.append(offset);

	if (ref != null) {
	    ref.print(result, offset, processed);
	}

	return result.toString();
    }

    public String print() {
	return print(new StringBuffer(), "", new HashSet<Cell>());
    }

    public void printData(StringBuffer sb) {
	if (data != (char) 0) {
	    sb.append(data);
	}
    }

    public String printValues() {
	StringBuffer sb = new StringBuffer();
	Cell next = value;
	while (next != null) {
	    if (next.value != null) {
		next.value.printData(sb);
	    }
	    next = next.ref;
	}

	return sb.toString();
    }

    public String printRefs() {
	StringBuffer sb = new StringBuffer();
	Cell next = ref;
	while (next != null) {
	    if (next.value != null) {
		next.value.printData(sb);
	    }
	    next = next.ref;
	}

	return sb.toString();
    }

    /* tests */
    public static void testSpeed() {
	int max = 100000;
	int maxSamples = 100;
	int sample = 0;

	double cellPush = 0;
	double cellPop = 0;
	double dequePush = 0;
	double dequePop = 0;

	while (sample++ < maxSamples) {
	    long startTime = System.currentTimeMillis();
	    Cell head = new Cell();

	    for (int i = 0; i < max; i++) {
		char val = (char) (Math.random() * 60000);
		head.ref = new Cell(val, head.ref);
	    }
	    cellPush += (System.currentTimeMillis() - startTime);
	    // System.out.println("Cell pushing " + max + " took: " + (System.currentTimeMillis() - startTime) + "ms");

	    startTime = System.currentTimeMillis();
	    while (head != null) {
		char data = head.data;
		head = head.ref;
	    }
	    cellPop += (System.currentTimeMillis() - startTime);
	    // System.out.println("Cell popping " + count + " took: " + (System.currentTimeMillis() - startTime) +
	    // "ms");

	    Deque<Character> deque = new ArrayDeque<Character>();
	    startTime = System.currentTimeMillis();
	    for (int i = 0; i < max; i++) {
		char val = (char) (Math.random() * 60000);
		deque.push(val);
	    }
	    dequePush += (System.currentTimeMillis() - startTime);
	    // System.out.println("Deque pushing " + count + " took: " + (System.currentTimeMillis() - startTime) +
	    // "ms");
	    startTime = System.currentTimeMillis();
	    while (deque.size() > 0) {
		char data = deque.pop();
	    }
	    dequePop += (System.currentTimeMillis() - startTime);
	    // System.out.println("Deque popping " + count + " took: " + (System.currentTimeMillis() - startTime) +
	    // "ms");
	}
	System.out.println("Cell pushing " + max + " took: " + cellPush / maxSamples + "ms");
	System.out.println("Cell popping " + max + " took: " + cellPop / maxSamples + "ms");
	System.out.println("Deque pushing " + max + " took: " + dequePush / maxSamples + "ms");
	System.out.println("Deque popping " + max + " took: " + dequePop / maxSamples + "ms");
    }

    public static void testDigest() {
	String input = "Semantics (from Greek sēmantiká, neuter plural of sēmantikós)[1][2] is the study of meaning. It focuses on the relation between signifiers, such as words, phrases, signs and symbols, and what they stand for, their denotata.\n"
		+ "\n"
		+ "Linguistic semantics is the study of meaning that is used by humans to express themselves through language. Other forms of semantics include the semantics of programming languages, formal logics, and semiotics.\n"
		+ "\n"
		+ "The word \"semantics\" itself denotes a range of ideas, from the popular to the highly technical. It is often used in ordinary language to denote a problem of understanding that comes down to word selection or connotation. This problem of understanding has been the subject of many formal inquiries, over a long period of time, most notably in the field of formal semantics. In linguistics, it is the study of interpretation of signs or symbols as used by agents or communities within particular circumstances and contexts.[3] Within this view, sounds, facial expressions, body language, proxemics have semantic (meaningful) content, and each has several branches of study. In written language, such things as paragraph structure and punctuation have semantic content; in other forms of language, there is other semantic content.[3]\n"
		+ "\n"
		+ "The formal study of semantics intersects with many other fields of inquiry, including lexicology, syntax, pragmatics, etymology and others, although semantics is a well-defined field in its own right, often with synthetic properties.[4] In philosophy of language, semantics and reference are closely connected. Further related fields include philology, communication, and semiotics. The formal study of semantics is therefore complex.\n"
		+ "\n"
		+ "Semantics contrasts with syntax, the study of the combinatorics of units of a language (without reference to their meaning), and pragmatics, the study of the relationships between the symbols of a language, their meaning, and the users of the language.[5]\n"
		+ "\n" + "In international scientific vocabulary semantics is also called semasiology.";

	Cell index = new Cell();
	// index.digest(input);
	index.index(input);

	Cell found = index.search("semantics");
	if (found != null) {
//	    System.out.println(found.printRefs());
	    System.out.println(found.print());
	}
//	Interval interval = index.findMatchPos("Z");
//	System.out.println(interval);
//	System.out.println("found: " + input.substring((int) interval.getStart(), (int) interval.getEnd()));
//	System.out.println("before: " + input.substring(0, (int) interval.getStart()));
    }

    public static void learn() {
	Cell cell = new Cell();
	int max = 10;// (Integer.MAX_VALUE-1)/2;
	int correctResponsesInARow = 0;
	int lessons = 0;
	while (correctResponsesInARow < 100) {
	    int t1 = (int) (Math.random() * max);
	    int t2 = (int) (Math.random() * max);
	    int add = t1 + t2;
	    String query = t1 + "+" + t2 + "=";
	    String teach = query + add;
	    Cell response = cell.search(teach);
	    lessons++;
	    if (response == null) {
		correctResponsesInARow = 0;
		cell.digest(teach);
		// System.out.println("teached: "+teach);
		// if(cell.search(teach) == null) {
		// System.out.println("Failed to learn: "+teach);
		// System.out.println(cell.search(query).printRefs());
		// }
	    } else {
		correctResponsesInARow++;
		// System.out.println("knew: "+teach+" "+cell.search(query).print());
	    }
	}
	System.out.println("learned in " + lessons + " lessons.");
	System.out.println(cell.search("=7").print());
    }

    public static void main(String[] args) {
	// learn();
	 testDigest();
    }
}
