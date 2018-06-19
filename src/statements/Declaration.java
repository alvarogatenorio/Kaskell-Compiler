package statements;

import java.io.BufferedWriter;
import java.util.List;

import expressions.Identifier;
import kaskell.Definition;
import kaskell.SymbolTable;
import types.ArrayType;
import types.StructType;
import types.Type;

public class Declaration implements BasicStatement, Definition {
	private Type type;
	private Identifier identifier;
	private Identifier structIdentifier;

	/*
	 * Here we store the dimensions of an array of structs, we need to store this,
	 * because we explicitly create the type here, when working with normal arrays,
	 * the type was created previously
	 */
	private List<Integer> structDimensions;

	/* Something like "kinkeger a" or "[4][1]kinkeger" */
	public Declaration(Type type, Identifier identifier) {
		this.type = type;
		this.identifier = identifier;
		this.structIdentifier = null;
		this.structDimensions = null;
	}

	/* Something like "A a" being "A" a previously defined struct */
	public Declaration(Identifier structIdentifier, Identifier identifier) {
		this.identifier = identifier;
		this.structIdentifier = structIdentifier;
		this.type = null;
		this.structDimensions = null;
	}

	/* Something like "[7][5][8]A a" being "A" a previously defined struct */
	public Declaration(Identifier structIdentifier, List<Integer> structDimensions, Identifier identifier) {
		this.identifier = identifier;
		this.structIdentifier = structIdentifier;
		this.structDimensions = structDimensions;
		this.type = null;
	}

	/* Nothing to check, just assign a type to the identifier */
	@Override
	public boolean checkType() {
		identifier.setType(type);
		return true;
	}

	/*
	 * A declaration is a definition, so tries to insert the identifier in the
	 * symbol table (this may give a duplicate error)
	 */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		/* Declaration of an struct type */
		if (this.type == null) {
			/* Making weird things to assign the type */
			Type auxType = symbolTable.searchStructType(structIdentifier);
			if (structDimensions == null) {
				this.type = auxType;
			} else {
				this.type = new ArrayType((StructType) (auxType), structDimensions);
			}
			if (this.type == null) {
				return false;
			}
		}
		return symbolTable.insertIdentifier(this.identifier, this);
	}

	/* Just returns the types */
	@Override
	public Type getDefinitionType() {
		return this.type;
	}

	public Identifier getIdentifier() {
		return this.identifier;
	}

	public int getSize() {
		return type.getSize();
	}

	public int getAddress(SymbolTable symbolTable) {
		return 5 + symbolTable.getAccumulation() - this.getSize();
	}

	@Override
	public void generateCode(BufferedWriter bw) {
		// TODO Auto-generated method stub
		
	}
}
