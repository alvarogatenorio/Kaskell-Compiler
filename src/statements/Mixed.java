package statements;

import expressions.Expression;
import expressions.Identifier;
import kaskell.SymbolTable;
import types.Type;

public class Mixed extends BasicStatement {
	private Type type;
	private Expression expression;

	public Mixed(Type type, Identifier identifier, Expression expression) {
		this.type = type;
		this.identifier = identifier;
		this.expression = expression;
	}

	@Override
	public boolean checkType() {
		return false;
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		/* The order is important to avoid infinite recursions */
		return expression.checkIdentifiers(symbolTable) && symbolTable.insertIdentifier(identifier, this);
	}
}
