package statements;

import expressions.Identifier;
import kaskell.Definition;
import kaskell.SymbolTable;
import types.Type;

public class Declaration implements BasicStatement, Definition {
	private Type type;
	private Identifier identifier;

	public Declaration(Type type, Identifier identifier) {
		this.type = type;
		this.identifier = identifier;
	}

	/* Nothing to check, just assign a type to the identifier */
	@Override
	public boolean checkType() {
		identifier.setType(type);
		return true;
	}

	/*
	 * A declaration is a definition, so tries to insert the identifier in the
	 * symbol table (this may give a duplicate error)
	 */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return symbolTable.insertIdentifier(this.identifier, this);
	}

	/* Just returns the types */
	@Override
	public Type getType() {
		return this.type;
	}
}
