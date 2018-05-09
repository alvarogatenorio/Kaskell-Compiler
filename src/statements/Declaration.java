package statements;

import expressions.Identifier;
import kaskell.SymbolTable;
import types.Type;

public class Declaration extends BasicStatement {
	private Type type;

	public Declaration(Type type, Identifier identifier) {
		this.type = type;
		this.identifier = identifier;
	}

	@Override
	public boolean checkType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return symbolTable.insertIdentifier(this.identifier, this);
	}
}
