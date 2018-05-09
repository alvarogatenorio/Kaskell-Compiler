package statements;

import expressions.Expression;
import kaskell.Block;
import kaskell.SymbolTable;

public class While extends ComplexStatement {
	private Expression condition;

	public While(Expression condition, Block body) {
		this.condition = condition;
		this.body = body;
	}

	@Override
	public boolean checkType() {
		return false;
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = condition.checkIdentifiers(symbolTable);
		if (wellIdentified) {
			symbolTable.startBlock();
			wellIdentified = body.checkIdentifiers(symbolTable);
			symbolTable.closeBlock();
		}
		return wellIdentified;
	}
}
