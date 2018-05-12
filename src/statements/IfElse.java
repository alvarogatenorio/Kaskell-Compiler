package statements;

import expressions.Expression;
import kaskell.Block;
import kaskell.SymbolTable;

public class IfElse extends If {
	private Block elseBody;

	public IfElse(Expression condition, Block thenBody, Block elseBody) {
		super(condition, thenBody);
		this.elseBody = elseBody;
	}
	
	/*Checks the if-then part and the else block*/
	public boolean checkType() {
		return super.checkType() && this.elseBody.checkType();
	}

	/* Checks the conditions, the "then" block and the "else" block */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = super.checkIdentifiers(symbolTable);
		if (wellIdentified) {
			symbolTable.startBlock();
			wellIdentified = elseBody.checkIdentifiers(symbolTable);
			symbolTable.closeBlock();
		}
		return wellIdentified;
	}

}
