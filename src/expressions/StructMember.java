package expressions;

import java.util.ArrayList;
import java.util.List;

import kaskell.SymbolTable;
import statements.Declaration;
import types.ArrayType;
import types.StructType;
import types.Type;

public class StructMember extends Identifier implements Expression {

	private List<Identifier> identifiers;
	private List<Type> matchedTypes;

	public StructMember(List<Identifier> identifiers) {
		super(null);
		this.identifiers = identifiers;
		this.matchedTypes = new ArrayList<Type>();
	}

	@Override
	public boolean checkType() {
		/* We need to be well identified */
		if ((identifiers.size() - 1) == matchedTypes.size()) {
			/* Check for each identifier in the list */
			for (int i = 0; i < matchedTypes.size() - 1; i++) {
				/* Types have to be both simple or array */
				if (identifiers.get(i + 1) instanceof ArrayIdentifier) {
					if (matchedTypes.get(i) instanceof ArrayType) {
						/* Both types have to be of the same dimension */
						if (((ArrayType) (matchedTypes.get(i)))
								.getSize() != ((ArrayIdentifier) (identifiers.get(i + 1))).getSize()) {
							return false;
							/* Everything is well typed */
						} else if (!((ArrayIdentifier) (identifiers.get(i + 1))).checkType()) {
							return false;
						}
					} else {
						return false;
					}
				} else {
					/* The last is special */
					if (matchedTypes.get(i) instanceof ArrayType && i != matchedTypes.size() - 1) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		/* check the first identifier */
		boolean wellIdentified = identifiers.get(0).checkIdentifiers(symbolTable);
		if (wellIdentified) {
			/* Don't look at the first! */
			for (int i = 1; i < identifiers.size(); i++) {
				/*
				 * For each identifier in the chain a.b. ... .c, checks if matches with an
				 * identifier of the corresponding struct
				 */
				wellIdentified = wellIdentified && checkIdentifiersAux(
						/* The previous was an struct type */
						(ArrayList<Declaration>) ((StructType) (identifiers.get(i - 1).getType())).getDeclarations(),
						identifiers.get(i));
			}
		}
		return wellIdentified;
	}

	private boolean checkIdentifiersAux(List<Declaration> declarations, Identifier identifier) {
		for (int i = 0; i < declarations.size(); i++) {
			if (declarations.get(i).getIdentifier().equals(identifier)) {
				identifier.setType(declarations.get(i).getDefinitionType());
				this.matchedTypes.add(declarations.get(i).getDefinitionType());
				return true;
			}
		}
		System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
				+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is not defined in this scope");
		return false;
	}

	/* The type of a.b. ... .c is the type of c */
	@Override
	public Type getType() {
		if (matchedTypes.size() == identifiers.size() - 1) {
			return this.matchedTypes.get(matchedTypes.size() - 1);
		}
		return null;
	}

	@Override
	public int getRow() {
		return identifiers.get(0).getRow();
	}

	@Override
	public int getColumn() {
		return identifiers.get(0).getColumn();
	}

}
