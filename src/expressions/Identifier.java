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
		type = null;
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

	/*
	 * If we reach this point, everything is well identified, so, in particular, all
	 * identifiers have a type
	 */
	@Override
	public boolean checkType() {
		return true;
	}

	/* Just checks itself and get a type if doesn't have one yet */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = symbolTable.searchIdentifier(this);
		if (wellIdentified && (type == null)) {
			this.type = symbolTable.searchIdentifierType(this);
		}
		return wellIdentified;
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
