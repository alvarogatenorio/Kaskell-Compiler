package statements;

import expressions.Expression;
import expressions.Identifier;
import kaskell.SymbolTable;

public class Assignment implements BasicStatement {
	protected Expression expression;
	protected Identifier identifier;

	public Assignment(Identifier identifier, Expression expression) {
		this.identifier = identifier;
		this.expression = expression;
	}

	/*
	 * Checks the type of the expression and then checks if it equals the type of
	 * the identifier
	 */
	public boolean checkType() {
		return expression.checkType() && (identifier.getType().equals(expression.getType()));
	}

	/* Checks the expression and the identifier (order matters) */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return expression.checkIdentifiers(symbolTable) && identifier.checkIdentifiers(symbolTable);
	}
}
