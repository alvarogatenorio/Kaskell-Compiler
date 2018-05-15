package statements;

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
	private List<Integer> structDimensions;

	public Declaration(Type type, Identifier identifier) {
		this.type = type;
		this.identifier = identifier;
		this.structIdentifier = null;
		this.structDimensions = null;
	}

	public Declaration(Identifier structIdentifier, Identifier identifier) {
		this.identifier = identifier;
		this.structIdentifier = structIdentifier;
		this.type = null;
		this.structDimensions = null;
	}

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
}
