package expressions;

import kaskell.SymbolTable;
import types.Type;

public class Identifier implements Expression {
	private String s;
	private int row;
	private int column;
	private Type type;

	public Identifier(String s) {
		this.s = s;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return this.row;
	}

	public int getColumn() {
		return this.column;
	}

	@Override
	public boolean checkType() {
		return true;
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return symbolTable.searchIdentifier(this);
	}

	@Override
	public Type getType() {
		return this.type;
	}

	public Type getSimpleType() {
		return new Type(this.type.getType());
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String toString() {
		return s;
	}

}
