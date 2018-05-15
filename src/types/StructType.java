package types;

import java.util.List;

import expressions.Identifier;
import kaskell.Definition;
import kaskell.SymbolTable;
import statements.Declaration;

/*Structs are like tuples, order matters*/
public class StructType extends Type implements Definition {
	private Identifier identifier;
	private List<Declaration> declarations;

	public StructType(Identifier identifier, List<Declaration> declaration) {
		super(null);
		this.identifier = identifier;
		this.declarations = declaration;
	}

	/* Provisional constructor for functions with struct arguments */
	public StructType(Identifier identifier) {
		super(null);
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
		if (other.getType() == null) {
			if (!(other instanceof ArrayType)) {
				StructType aux = (StructType) (other);
				if (aux.getDeclarations().size() == this.declarations.size()) {
					for (int i = 0; i < this.declarations.size(); i++) {
						if (this.declarations.get(i).getDefinitionType() != aux.getDeclarations().get(i)
								.getDefinitionType()) {
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

}
