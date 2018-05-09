package functions;

import expressions.Identifier;
import kaskell.SymbolTable;

public class Function {
	private FunctionHead head;
	private FunctionTail tail;

	public Function(FunctionHead head, FunctionTail tail) {
		this.head = head;
		this.tail = tail;
	}

	public boolean checkType() {
		return false;
	}

	public boolean checkIdentifiers(SymbolTable symbolTable) {
		symbolTable.startBlock();
		boolean wellIdentified = tail.checkIdentifiers(symbolTable);
		symbolTable.closeBlock();
		return wellIdentified;
	}

	public Identifier getIdentifier() {
		return this.head.getIdentifier();
	}
}
