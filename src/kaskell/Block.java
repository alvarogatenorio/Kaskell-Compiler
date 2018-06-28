package kaskell;

import java.util.List;

import statements.Statement;

/*Remember a block is also a statement!*/
public class Block implements Statement {
	private List<Statement> statements;

	public Block(List<Statement> statements) {
		this.statements = statements;
	}

	public List<Statement> getStatements() {
		return this.statements;
	}

	/* Checks each statement identifier */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = true;
		/* There may be a return block with only a return statement... */
		if (statements != null) {
			for (int i = 0; i < statements.size(); i++) {
				/* Recursive case */
				if (statements.get(i) instanceof Block) {
					symbolTable.startBlock();
					wellIdentified = wellIdentified && statements.get(i).checkIdentifiers(symbolTable);
					symbolTable.closeBlock();
				} else {
					wellIdentified = wellIdentified && statements.get(i).checkIdentifiers(symbolTable);
				}
			}
		}
		return wellIdentified;
	}

	/* Checks each statement, taking care of the return block case */
	@Override
	public boolean checkType() {
		boolean wellTyped = true;
		if (statements != null) {
			for (int i = 0; i < statements.size(); i++) {
				wellTyped = wellTyped && statements.get(i).checkType();
			}
		}
		return wellTyped;
	}

	/* Generates the code of each block */
	public void generateCode(Instructions instructions) {
		for (int i = 0; i < statements.size(); i++) {
			statements.get(i).generateCode(instructions);
		}
	}
}
