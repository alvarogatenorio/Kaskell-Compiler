package expressions;

import kaskell.SymbolTable;
import types.Type;
import types.Types;

public class DummyBoolean implements Expression {
	private Boolean b;

	public DummyBoolean(Boolean b) {
		this.b = b;
	}

	@Override
	public boolean checkType() {
		return true;
	}

	@Override
	public Type getType() {
		return new Type(Types.BOOLEAN);
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return true;
	}
}
