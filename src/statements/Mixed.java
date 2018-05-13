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

	/* Checks and assign type to the identifier */
	@Override
	public boolean checkType() {
		/* Checks the expression (well typed and type matching) */
		boolean wellTyped = expression.checkType() && (expression.getType().equals(type));
		/* Assign type to the identifier */
		identifier.setType(type);
		return wellTyped;
	}

	/*
	 * A mixed statement is a definition, so it tries to insert in the symbol table,
	 * but first checks if the expression is well identified
	 */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		/* The order is important! */
		return expression.checkIdentifiers(symbolTable) && symbolTable.insertIdentifier(identifier, this);
	}

	/* Just returns the type */
	@Override
	public Type getDefinitionType() {
		return this.type;
	}
}
