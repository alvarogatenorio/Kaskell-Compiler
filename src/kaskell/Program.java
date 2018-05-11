package kaskell;

import java.util.List;

import functions.Function;

/*Implements the dummy interface definition so we can insert
 * instances of program in the symbol table (not really necessary)*/
public class Program implements Definition {
	private List<Block> blocks;
	private List<Function> functions;
	private SymbolTable symbolTable = new SymbolTable();

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

	public boolean checkIdentifiers() {
		/*The first block is for global identifiers*/
		symbolTable.startBlock();
		boolean wellIdentified = true;
		/* First consider functions, you can call a function from another */
		if (functions != null) {
			for (int i = 0; i < functions.size(); i++) {
				wellIdentified = wellIdentified && symbolTable.insertIdentifier(functions.get(i).getIdentifier(), this);
				wellIdentified = wellIdentified && functions.get(i).checkIdentifiers(symbolTable);
			}
		}
		/*Checks the blocks (there must be at least one)*/
		for (int i = 0; i < blocks.size(); i++) {
			symbolTable.startBlock();
			wellIdentified = wellIdentified && blocks.get(i).checkIdentifiers(symbolTable);
			symbolTable.closeBlock();
		}
		return wellIdentified;
	}

	public boolean checkType() {
		boolean wellTyped = true;
		/*First checks the types of functions*/
		if (functions != null) {
			for (int i = 0; i < functions.size(); i++) {
				wellTyped = wellTyped && functions.get(i).checkType();
			}
		}
		/*Then checks the types of blocks*/
		for (int i = 0; i < blocks.size(); i++) {
			wellTyped = wellTyped && blocks.get(i).checkType();
		}
		return wellTyped;
	}
}
