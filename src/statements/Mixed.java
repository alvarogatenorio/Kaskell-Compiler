package statements;

import expressions.Expression;
import expressions.Identifier;
import kaskell.Definition;
import kaskell.SymbolTable;
import types.Type;
import types.Types;

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
		if(!expression.getType().equals(type)) {
			System.err.println("TYPE ERROR: in line " + (this.expression.getRow() + 1) + " column "
					+ (this.expression.getColumn() + 1)
					+ " fatal error the expression typed wrong for this place!");
			return false;
		}
		/* Checks the expression (well typed and type matching) */
		boolean wellTyped = expression.checkType();
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
