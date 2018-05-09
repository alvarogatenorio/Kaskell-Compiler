package statements;

import expressions.Expression;
import kaskell.Block;
import kaskell.SymbolTable;

public class If extends ComplexStatement {
	protected Expression condition;

	public If(Expression condition, Block thenBody) {
		this.condition = condition;
		this.body = thenBody;
	}

	@Override
	public boolean checkType() {
		return this.condition.checkType() && this.body.checkType();
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
