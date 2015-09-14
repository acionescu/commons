package ro.zg.util.data;

import java.util.ArrayList;
import java.util.List;

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
