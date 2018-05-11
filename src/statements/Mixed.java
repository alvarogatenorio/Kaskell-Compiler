package statements;

import expressions.Expression;
import expressions.Identifier;
import kaskell.Definition;
import kaskell.SymbolTable;
import types.Type;

public class Mixed implements BasicStatement, Definition {
	private Type type;
	private Expression expression;
	private Identifier identifier;

	public Mixed(Type type, Identifier identifier, Expression expression) {
		this.type = type;
		this.identifier = identifier;
		this.expression = expression;
	}

	@Override
	public boolean checkType() {
		boolean wellTyped = expression.checkType() && (expression.getType() == type);
		System.err.println("TYPE ERROR: in line " + identifier.getRow() + " column " + identifier.getColumn()
				+ " types doesn't match");
		return wellTyped;
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		/* The order is important! */
		return expression.checkIdentifiers(symbolTable) && symbolTable.insertIdentifier(identifier, this);
	}
}
