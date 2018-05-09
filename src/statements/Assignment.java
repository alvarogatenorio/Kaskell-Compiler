package statements;

import expressions.Expression;
import expressions.Identifier;
import kaskell.SymbolTable;

public class Assignment extends BasicStatement {
	protected Expression expression;

	public Assignment(Identifier identifier, Expression expression) {
		this.identifier = identifier;
		this.expression = expression;
	}

	public boolean checkType() {
		return false;
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return identifier.checkIdentifiers(symbolTable) && expression.checkIdentifiers(symbolTable);
	}
}
