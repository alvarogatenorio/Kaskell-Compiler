package expressions;

import kaskell.SymbolTable;
import types.Type;

public class Identifier implements Expression {
	private String s;
	private int row;
	private int column;

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return symbolTable.searchIdentifier(this);
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString() {
		return s;
	}

}
