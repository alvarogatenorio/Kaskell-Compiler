package expressions;

import kaskell.SymbolTable;
import types.Type;
import types.Types;

public class DummyBoolean implements Expression {
	private Boolean b;
	private int row;
	private int column;

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

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public int getRow() {
		return this.row;
	}

	@Override
	public int getColumn() {
		return this.column;
	}
}
