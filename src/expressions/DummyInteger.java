package expressions;

import kaskell.SymbolTable;
import types.Type;
import types.Types;

public class DummyInteger implements Expression {
	private Integer i;
	private int row;
	private int column;

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
