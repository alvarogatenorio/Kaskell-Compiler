package statements;

import java.io.BufferedWriter;

import kaskell.Block;
import kaskell.SymbolTable;

public class For extends ComplexStatement {
	private ForTuple conditions;

	public For(ForTuple conditions, Block body) {
		this.conditions = conditions;
		this.body = body;
	}

	/* Just checks conditions and body */
	@Override
	public boolean checkType() {
		return this.conditions.checkType() && this.body.checkType();
	}

	/* Checks the conditions and the body */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		symbolTable.startBlock();
		boolean wellIdentified = conditions.checkIdentifiers(symbolTable) && body.checkIdentifiers(symbolTable);
		symbolTable.closeBlock();
		return wellIdentified;
	}

	@Override
	public void generateCode(BufferedWriter bw) {
		// TODO Auto-generated method stub
		
	}
}
