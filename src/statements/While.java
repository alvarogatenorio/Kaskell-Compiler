package statements;

import expressions.Expression;
import kaskell.Block;
import kaskell.SymbolTable;
import types.Type;
import types.Types;

public class While extends ComplexStatement {
	private Expression condition;

	public While(Expression condition, Block body) {
		this.condition = condition;
		this.body = body;
	}

	/* Checks if the condition is well typed and boolean, then checks the body */
	@Override
	public boolean checkType() {
		return condition.checkType() && (condition.getType().equals(new Type(Types.BOOLEAN))) && body.checkType();
	}

	/* Just checks the condition and the body block */
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
