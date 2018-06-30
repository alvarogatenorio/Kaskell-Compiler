package expressions;

import kaskell.Instructions;
import kaskell.SymbolTable;
import types.ArrayType;
import types.StructType;
import types.Type;

public class Identifier implements Expression {

	/*
	 * Note: Each identifier always obtain its type in the "check identifiers"
	 * phase, until then, it is null
	 */

	private String s;
	private int row;
	private int column;
	private boolean insideStructType;
	protected Type type;
	protected int address;
	protected int deltaDepth;
	private boolean reference;

	public Identifier(String s) {
		this.s = s;
		type = null;
		address = -1;
		deltaDepth = 0;
		insideStructType = false;
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
	 * identifiers have a type (obviously)
	 */
	@Override
	public boolean checkType() {
		return true;
	}

	/*
	 * Just checks itself and gets a type and address
	 */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		/*
		 * Search itself in the symbol table, if we don't find ourselves we can go home,
		 * because we weren't defined previously
		 */
		boolean wellIdentified = true;
		/* This says what to do when the identifier is a field of an StructType */
		if (insideStructType) {
			this.type = symbolTable.searchStructFieldType(this);
			this.address = symbolTable.getAccumulation() - this.type.getSize();
		} else {
			wellIdentified = symbolTable.searchIdentifier(this);
			if (wellIdentified) {
				/*
				 * We get our type and address with the help of the symbol table (looks at the
				 * pointer to the definition and gets the type of the definition, which is the
				 * type of the identifier)
				 */
				this.type = symbolTable.searchIdentifierType(this);
				this.address = symbolTable.searchIdentifierAddress(this);
				this.deltaDepth = symbolTable.getDepth() - symbolTable.searchDefinitionDepth(this);
			}
		}
		return wellIdentified;
	}

	@Override
	public Type getType() {
		return this.type;
	}

	public int getAddress() {
		return this.address;
	}

	/*
	 * If the type is a non-array one, the effect is the same as getType(), but if
	 * it is an ArrayType we just return the baseType, this is useful in the check
	 * types phase, for example when checking a[1][2]+3, a has the type ArrayType of
	 * integer, but the type of a[1][2] is just an integer...
	 * 
	 * Maybe it is better to overload the method getType in the ArrayIdentifier
	 * class...
	 */
	public Type getSimpleType() {
		/* The type is an StructType */
		if (this.type instanceof StructType) {
			return this.type;
		}
		/* If the type is array of StructType return only the base type */
		if (this.type instanceof ArrayType && (this.type.getType() == null)) {
			return ((ArrayType) this.type).getComplex();
		}
		/*
		 * The type is a simple type or an array of a simple type, in which case we
		 * return only the base type
		 */
		return new Type(this.type.getType());
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String toString() {
		return s;
	}

	public void setInsideStructType(boolean insideStructType) {
		this.insideStructType = insideStructType;
	}

	public boolean equals(Identifier other) {
		return this.s.equals(other.toString());
	}

	@Override
	public void generateCode(Instructions instructions) {
		// instructions.add("ldc " + this.address + ";\n");
		instructions.add("lda " + this.deltaDepth + " " + this.address + ";\n");
		instructions.add("ind;\n");
	}

	/* Used in function calls */
	public void setDeltaDepth(int d) {
		this.deltaDepth = d;
	}

	public int getDeltaDepth() {
		return this.deltaDepth;
	}

	/*
	 * A dirty shortcut to assign addresses, used only when assigning addresses to
	 * function parameters
	 */
	public void setAddress(int a) {
		this.address = a;
	}
}
