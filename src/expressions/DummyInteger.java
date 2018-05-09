package expressions;

import kaskell.SymbolTable;
import types.Type;
import types.Types;

public class DummyInteger implements Expression {
	private Integer i;

	public DummyInteger(Integer i) {
		this.i = i;
	}

	@Override
	public boolean checkType() {
		return true;
	}

	@Override
	public Type getType() {
		return new Type(Types.INTEGER);
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return true;
	}
}
