package statements;

import expressions.Identifier;
import kaskell.Definition;
import kaskell.SymbolTable;
import types.Type;

public class Declaration implements Statement, Definition {
	private Type type;
	private Identifier identifier;

	public Declaration(Type type, Identifier identifier) {
		this.type = type;
		this.identifier = identifier;
	}

	@Override
	public boolean checkType() {
		identifier.setType(type);
		return true;
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return symbolTable.insertIdentifier(this.identifier, this);
	}
}
