package statements;

import java.util.List;

import expressions.Identifier;
import functions.FunctionTail;
import kaskell.Definition;
import kaskell.Instructions;
import kaskell.SymbolTable;
import types.ArrayType;
import types.StructType;
import types.Type;

public class Declaration implements BasicStatement, Definition {

	/*
	 * This class has a little bit ----- weird structure -----
	 * 
	 * Remember objects of this class are going to be created primarily by the
	 * parser, so, if we have a simple declaration, such as an array of boolean or
	 * integer, the parser have by its own all the information required by the first
	 * constructor.
	 * 
	 * However, if we declare an StructType variable or an StructType array, the
	 * parser doesn't have the structType by itself, and I don't see the way in
	 * which it could have it without highly violating the
	 * "each module does only his task" principle we are following
	 */

	private boolean insideStructType;
	private boolean insideFunction;
	private FunctionTail functionInside;
	private int address;

	/* One may think we only need this attributes */
	private Type type;
	private Identifier identifier;

	/*
	 * ... but we also need the following two and the last two constructors for the
	 * StructType cases
	 */
	private Identifier structIdentifier;
	private List<Integer> structDimensions;

	/* Something like "type a" or "[4][1]type" */
	public Declaration(Type type, Identifier identifier) {
		this.type = type;
		this.identifier = identifier;
		this.structIdentifier = null;
		this.structDimensions = null;
		this.address = -1;
		this.insideStructType = false;
		this.insideFunction = false;
		this.functionInside = null;
	}

	/* Something like "A a" being "A" a previously defined StructType (hopefully) */
	public Declaration(Identifier structIdentifier, Identifier identifier) {
		this.identifier = identifier;
		this.structIdentifier = structIdentifier;
		this.type = null;
		this.structDimensions = null;
		this.address = -1;
		this.insideStructType = false;
		this.insideFunction = false;
		this.functionInside = null;
	}

	/*
	 * Something like "[7][5][8]A a" being "A" a previously defined StructType
	 * (hopefully)
	 */
	public Declaration(Identifier structIdentifier, List<Integer> structDimensions, Identifier identifier) {
		this.identifier = identifier;
		this.structIdentifier = structIdentifier;
		this.structDimensions = structDimensions;
		this.type = null;
		this.address = -1;
		this.insideStructType = false;
		this.insideFunction = false;
		this.functionInside = null;
	}

	/*
	 * ----- Notice that type will be null in the complex cases until the identifier
	 * check phase, in which will be finally defined (this is kind of an invariant)
	 * -----
	 */

	/*
	 * If this is well identified, is, by definition, well typed, so there is
	 * nothing to check
	 */
	@Override
	public boolean checkType() {
		return true;
	}

	/*
	 * First we try to define our type in the complex cases (StructTypes and arrays
	 * of StructTypes) This is kind of slightly mixing the identifiers and types
	 * phases, but don't see a much better way to do this.
	 * 
	 * A declaration is a definition, so tries to insert the identifier in the
	 * symbol table (this may give a "duplicate" error in the symbol table).
	 */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		/* If we are declaring an StructType thing (see invariant) */
		if (this.type == null) {
			/*
			 * We search in the symbol table for the StructType identifier, notice it will
			 * return null if the StructType is not found!
			 */
			Type auxType = symbolTable.searchStructType(structIdentifier);
			/* If we are declaring a simple StructType variable */
			if (structDimensions == null) {
				this.type = auxType;
			} else { /* If we are declaring a StructType array */
				this.type = new ArrayType((StructType) (auxType), structDimensions);
			}
			/* If the type was not found, we go home */
			if (this.type == null) {
				return false;
			}
		}
		/* Inserting the identifier in the symbol table */
		boolean wellIdentified = symbolTable.insertIdentifier(this.identifier, this);
		if (wellIdentified) {
			/*
			 * We can be here when checking the identifiers of the original declaration of
			 * an StructType and this may be a source of endless errors because of the
			 * not-look-at-the-first-two-blocks policy of the symbol table
			 */
			if (insideStructType) {
				this.identifier.setInsideStructType(true);
				/* The address will be set in the identifier when checking indentifiers */

			} else if (insideFunction) {
				this.identifier.setInsideFunction(true);
				this.identifier.setFunctionInside(functionInside);
				int argOffset = 0;
				if (functionInside.getArguments() != null) {
					argOffset = functionInside.getArguments().size();
				}
				this.address = 5 + argOffset + symbolTable.getAccumulation() - this.getSize();
			} else { // ordinary case
				this.address = 5 + symbolTable.getAccumulation() - this.getSize();
			}
			/*
			 * This is necessary for the identifier on getting its type and address
			 * properties.
			 */
			this.identifier.checkIdentifiers(symbolTable);
		}
		return wellIdentified;
	}

	/*
	 * Just returns the type, notice it may return null if used before the
	 * identifiers phase (be careful)
	 */
	@Override
	public Type getDefinitionType() {
		return this.type;
	}

	public Identifier getIdentifier() {
		return this.identifier;
	}

	/* Returns the type size */
	public int getSize() {
		return type.getSize();
	}

	public int getAddress(SymbolTable symbolTable) {
		return this.address;
	}

	public void setInsideStructType(boolean insideStructType) {
		this.insideStructType = insideStructType;
	}

	public void setInsideFunction(boolean insideFunction) {
		this.insideFunction = insideFunction;
	}

	public void setFunctionInside(FunctionTail functionInside) {
		this.functionInside = functionInside;
	}

	@Override
	public void generateCode(Instructions instructions) {
		/* Declarations don't generate code by themselves */
	}
}
