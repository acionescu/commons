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

import net.segoia.util.data.Cell;
import net.segoia.util.data.Interval;

import org.junit.Ignore;
import org.junit.Test;

public class CellTest {
    char data;

    @Test
    @Ignore
    public void testMatch() {
	Cell index = new Cell();
	index.indexMaximal(";*UaVuH)dtjg;BÁmÂ{I>Z5I");
	System.out.println(index.lastRef.value.data);
	Interval i = index.findMatchPos(";*UaVuH);BÁmÂ{I>Z5I");
	System.out.println(i);

	// data+=2;
	data--;
	System.out.println((int) data);
    }

    @Test
    public void testPrintIndexed() {
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
	index.indexMinimal(input);

	StringBuffer sb = new StringBuffer();
//	index.printIndexed(sb, 0, false);
//	System.out.println(sb);

	Cell stopChars = new Cell();
	stopChars.tokens(" ,.[]{}();:'\"");

	 System.out.println(index.getRefValue(' ', false).printValuesWithDepth(new StringBuffer(), 5, "", stopChars, 0));

	// System.out.println(index.getRefValue('o', false).printRefsWithStrength());
	
    }

    @Test
    public void testRandomCell() {
	Cell c = generateRandomExecutionCell(10);
	System.out.println("----------------------------------");
//	System.out.println(c.data);

	System.out.println(c.printValuesWithDepth(new StringBuffer(), 10, "", null, 0));
	
	Cell t = new Cell();
	t.indexMinimal("This is a test!");
	
	
	Cell result = c.execute(t.ref);
	System.out.println(result.printValuesWithDepth(new StringBuffer(), 100, "", null, 0));
    }

    private Cell generateRandomExecutionCell(int length) {
	Cell out = null;
	for (int i = 0; i < length; i++) {
	    int ri = (int) (Math.random() * (Cell.operations.length - 1));
	    char data = Cell.operations[ri];
	    Cell c = new Cell(data);
	    c.value = out;
	    out = c;
	}
	return out;
    }
}
