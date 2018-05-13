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

	/*
	 * Checks if the condition is a boolean expression and if the condition and the
	 * body are well typed
	 */
	@Override
	public boolean checkType() {
		if(!condition.getType().equals(new Type(Types.BOOLEAN))) {
			System.err.println("TYPE ERROR: in line " + (this.condition.getRow() + 1) + " column "
					+ (this.condition.getColumn() + 1)
					+ " fatal error the condition is not Koolean type!");
			return false;
		}
		
		return condition.checkType() && body.checkType();
	}

	/* Checks the condition and the body block */
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
