package expressions;

import java.io.BufferedWriter;

import kaskell.SymbolTable;
import types.ArrayType;
import types.StructType;
import types.Type;

public class Identifier implements Expression {
	private String s;
	private int row;
	private int column;
	protected Type type;

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
		/* The type is really null */
		if (this.type == null) {
			return null;
		}
		/* The type is an struct */
		if (this.type instanceof StructType) {
			return this.type;
		}
		/* The type is array of structs */
		if (this.type instanceof ArrayType && (this.type.getType() == null)) {
			return ((ArrayType) this.type).getComplex();
		}
		return new Type(this.type.getType());
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String toString() {
		return s;
	}

	public boolean equals(Identifier other) {
		return this.s.equals(other.toString());
	}

	@Override
	public void generateCode(BufferedWriter bw) {
		// TODO Auto-generated method stub
		
	}
}
