package kaskell;

import java.util.List;

import statements.Statement;

public class Block implements Statement {
	private List<Statement> statements;

	public Block(List<Statement> statements) {
		this.statements = statements;
	}

	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = true;
		for (int i = 0; i < statements.size(); i++) {
			if (statements.get(i) instanceof Block) {
				symbolTable.startBlock();
				wellIdentified = wellIdentified && statements.get(i).checkIdentifiers(symbolTable);
				symbolTable.closeBlock();
			} else {
				wellIdentified = wellIdentified && statements.get(i).checkIdentifiers(symbolTable);
			}
		}
		return wellIdentified;
	}

	@Override
	public boolean checkType() {
		// TODO Auto-generated method stub
		return false;
	}
}
