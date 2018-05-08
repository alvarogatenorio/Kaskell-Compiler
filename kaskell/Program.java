package kaskell;

import java.util.List;

import functions.Function;

public class Program {
	private List<Block> blocks;
	private List<Function> functions;

	/* Constructor for programs without functions */
	public Program(List<Block> bs) {
		this.blocks = bs;
		this.functions = null;
	}

	/* Constructor for programs with functions */
	public Program(List<Block> bs, List<Function> fs) {
		this.blocks = bs;
		this.functions = fs;
	}
}
