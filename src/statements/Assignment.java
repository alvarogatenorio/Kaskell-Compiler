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

	public boolean checkType() {
		return expression.checkType() && (identifier.getType() == expression.getType());
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return identifier.checkIdentifiers(symbolTable) && expression.checkIdentifiers(symbolTable);
	}
}
