package kaskell;

import java.util.ArrayList;
import java.util.List;

public class Instructions {
	private List<String> instructions;
	/* Keeps the count of the number of real P-code instructions */
	private int counter;

	public Instructions() {
		this.instructions = new ArrayList<String>();
		this.counter = -1;
	}

	public void add(String s) {
		this.counter++;
		this.instructions.add("{" + this.counter + "} " + s);
	}

	public void addComment(String s) {
		this.instructions.add(s);
	}

	public int size() {
		return this.instructions.size();
	}

	public String get(int i) {
		return this.instructions.get(i);
	}

	public void remove(int i) {
		this.instructions.remove(i);
		this.counter--;
	}

	public void set(int i, String s) {
		this.instructions.set(i, s);
	}

	public int getCounter() {
		return this.counter;
	}
}
