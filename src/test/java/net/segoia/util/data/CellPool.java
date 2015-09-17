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

import java.util.ArrayList;
import java.util.List;

import net.segoia.util.data.Cell;

public class CellPool implements Runnable {
    private String id;
    private int maxPopulation = 100000;
    private List<Cell> population;
    private int maxCodeLength = 20;
    private int maxInteractionsPerIteration = 100;

    private boolean stopCondition = false;
    
    

    public CellPool(String id) {
	super();
	this.id = id;
    }

    public void init() {
	System.out.println("Initializing "+id);
	population = new ArrayList<Cell>();
	generatePopulation();
    }

    private void generatePopulation() {
	int count = 0;
	while (count++ < maxPopulation) {
	    population.add(generateRandomExecutionCell((int) (maxCodeLength * Math.random())));
	}
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
    
    private Cell generateRandomCell(int length) {
	Cell out = null;
	for (int i = 0; i < length; i++) {
	    char data = (char) (Math.random() * (65000));
	    Cell c = new Cell(data);
	    c.value = out;
	    out = c;
	}
	return out;
    }

    private void iteration() {
	int interactions = 0;
	Cell lastResult = null;
	while (interactions++ < maxInteractionsPerIteration) {
	    lastResult = newInteraction();
	}
	
	System.out.println( id+ " - "+lastResult.printValuesWithDepth(new StringBuffer(), 50, "", null, 0));
    }

    public void run() {
	try {
	    while (!stopCondition) {
		iteration();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private Cell newInteraction() {
	Cell executor = randomCell();
	Cell target = removeRandomCell();
	// System.out.println(executor);
	// System.out.println(target);
	Cell result = executor.execute(target);
	population.add(result);

	return result;
    }
    
    private int randomCellIndex() {
	return (int) ((population.size() - 1) * Math.random());
    }

    private Cell randomCell() {
	Cell out = null;
	while (out == null) {

	    out = population.get(randomCellIndex());
	}
	return out;
    }
    
    private Cell removeRandomCell() {
	return population.remove(randomCellIndex());
    }

    public static void main(String[] args) {
	int count=0;
	while (true) {
	    
	    CellPool cp = new CellPool("Pool "+(++count));
	    cp.init();

	    Thread t = new Thread(cp);

	    t.start();

	    try {
		t.join();
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    System.gc();
	}
    }
}
