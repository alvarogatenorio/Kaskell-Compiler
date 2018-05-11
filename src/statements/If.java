package statements;

import expressions.Expression;
import kaskell.Block;
import kaskell.SymbolTable;
import types.Type;
import types.Types;

public class If extends ComplexStatement {
	protected Expression condition;

	public If(Expression condition, Block thenBody) {
		this.condition = condition;
		this.body = thenBody;
	}

	@Override
	public boolean checkType() {
		return condition.checkType() && (condition.getType() == new Type(Types.BOOLEAN)) && body.checkType();
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
