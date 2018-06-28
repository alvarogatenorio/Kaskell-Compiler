package types;

import java.util.List;

import expressions.Identifier;
import kaskell.Definition;
import kaskell.SymbolTable;
import statements.Declaration;

/* StructTypes are like tuples, order matters
 * ----- Invariant: The "types" attribute is always null -----*/
public class StructType extends Type implements Definition {
	private Identifier identifier;
	private List<Declaration> declarations;

	public StructType(Identifier identifier, List<Declaration> declaration) {
		super(null); // love the invariant
		this.identifier = identifier;
		this.declarations = declaration;
		/* Setting the declarations as inside of structTypes */
		for (int i = 0; i < declarations.size(); i++) {
			declarations.get(i).setInsideStructType(true);
		}
	}

	/* Provisional constructor for functions with StructType arguments */
	public StructType(Identifier identifier) {
		super(null); // love the invariant
		this.identifier = identifier;
	}

	public List<Declaration> getDeclarations() {
		return this.declarations;
	}

	public Identifier getIdentifier() {
		return this.identifier;
	}

	/* Structural type equivalence */
	public boolean equals(Type other) {
		/* If the "types" of the other is not null, then is not a StructType */
		if (other.getType() == null) {
			/* If the other is an ArrayType, then is not a StructType */
			if (!(other instanceof ArrayType)) {
				/* Here, the other type is for sure a StructType */
				StructType aux = (StructType) (other);
				/*
				 * Two StructTypes are equals if and only if have the same type declarations in
				 * the same order
				 */
				if (aux.getDeclarations().size() == this.declarations.size()) {
					for (int i = 0; i < this.declarations.size(); i++) {
						if (!this.declarations.get(i).getDefinitionType()
								.equals(aux.getDeclarations().get(i).getDefinitionType())) {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	/* Checks each declaration */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		/* The temporary opened block is always the 0th block */
		symbolTable.startBlock();
		boolean wellIdentified = true;
		for (int i = 0; i < declarations.size(); i++) {
			wellIdentified = wellIdentified && declarations.get(i).checkIdentifiers(symbolTable);
		}
		symbolTable.closeBlock();
		return wellIdentified;
	}

	/* Checks each declaration */
	public boolean checkType() {
		boolean wellTyped = true;
		for (int i = 0; i < declarations.size(); i++) {
			wellTyped = wellTyped && declarations.get(i).checkType();
		}
		return true;
	}

	@Override
	public Type getDefinitionType() {
		return this;
	}

	public int getSize() {
		int size = 0;
		for (int i = 0; i < this.declarations.size(); i++) {
			size += declarations.get(i).getSize();
		}
		return size;
	}
}
