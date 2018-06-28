package expressions;

import java.util.ArrayList;
import java.util.List;

import kaskell.Instructions;
import kaskell.SymbolTable;
import statements.Declaration;
import types.ArrayType;
import types.StructType;
import types.Type;

public class StructMember extends Identifier implements Expression {

	/*
	 * This class has a little bit complicated checking methods, but I don't see how
	 * to make it simpler
	 */

	private List<Identifier> identifiers;
	private List<Type> matchedTypes;
	private int offset;

	public StructMember(List<Identifier> identifiers) {
		super(null);
		this.identifiers = identifiers;
		this.matchedTypes = new ArrayList<Type>();
		this.offset = 0;
	}

	@Override
	public boolean checkType() {
		/*
		 * If the first identifier in the chain is an array identifier but is type is
		 * not an ArrayType, obviously it is not well typed
		 */
		if (!(identifiers.get(0) instanceof ArrayIdentifier) && (identifiers.get(0).getType() instanceof ArrayType)) {
			System.err.println("TYPE ERROR: in line " + (this.identifiers.get(0).getRow() + 1) + " column "
					+ (this.identifiers.get(0).getColumn() + 1) + " fatal error cause by identifier type number " + 0
					+ "!");
			return false;
		}
		/* We need to be well identified, if not, we can go home */
		if ((identifiers.size() - 1) == matchedTypes.size()) {
			/* Check for each identifier in the list */
			for (int i = 0; i < matchedTypes.size() - 1; i++) {
				/* Types have to be both simple or array */
				if (identifiers.get(i + 1) instanceof ArrayIdentifier) {
					if (matchedTypes.get(i) instanceof ArrayType) {
						/* Both types have to be of the same dimension */
						if (((ArrayType) (matchedTypes.get(i)))
								.getSize() != ((ArrayIdentifier) (identifiers.get(i + 1))).getSize()) {
							int num = i + 1;
							System.err.println("TYPE ERROR: in line " + (this.identifiers.get(i + 1).getRow() + 1)
									+ " column " + (this.identifiers.get(i + 1).getColumn() + 1)
									+ " fatal error cause by identifier size number " + num + "!");
							return false;
							/* Everything is well typed */
						} else if (!((ArrayIdentifier) (identifiers.get(i + 1))).checkType()) {
							int num = i + 1;
							System.err.println("TYPE ERROR: in line " + (this.identifiers.get(i + 1).getRow() + 1)
									+ " column " + (this.identifiers.get(i + 1).getColumn() + 1)
									+ " fatal error cause by identifier type number " + num + "!");
							return false;
						}
					} else {
						System.err.println("TYPE ERROR: in line " + (this.identifiers.get(i + 1).getRow() + 1)
								+ " column " + (this.getColumn() + 1) + " fatal error cause by declaration member!");
						return false;
					}
				} else {
					/* The last is special */
					if (matchedTypes.get(i) instanceof ArrayType && i != matchedTypes.size() - 1) {
						int num = i + 1;
						System.err.println("TYPE ERROR: in line " + (this.getRow() + 1) + " column "
								+ (this.getColumn() + 1) + " fatal error cause by identifier type number " + num + "!");
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	/* Checks the chain of identifiers and sets its own type */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		/* check the first identifier, if it is not in the symbol table, go home */
		boolean wellIdentified = identifiers.get(0).checkIdentifiers(symbolTable);
		/*
		 * At this point, the address of the StructMember is the address of the first
		 * identifier, this will be taken into account in the code generation part
		 */
		this.address = identifiers.get(0).getAddress();
		if (wellIdentified) {
			/*
			 * Walk the rest of the identifiers in the list, but don't look at the first
			 * (just checked)
			 */
			for (int i = 1; i < identifiers.size(); i++) {
				/*
				 * For each identifier in the chain a.b. ... .c, checks if matches with an
				 * identifier of the corresponding StructType
				 */

				/*
				 * If the previous identifier wasn't a StructType or an array of StructType we
				 * can go home, we only want the cases a.b and a[2][2].b (for example)
				 */
				if (identifiers.get(i - 1).getType() instanceof StructType) {
					/*
					 * The previous was an StructType, here we just check if the current identifier
					 * matches with some of the declarations identifiers in the StructType
					 * represented by the previous identifier (a little bit messy code)
					 */
					wellIdentified = wellIdentified && checkIdentifiersAux(
							(ArrayList<Declaration>) ((StructType) (identifiers.get(i - 1).getType()))
									.getDeclarations(),
							identifiers.get(i));
				} else if (identifiers.get(i - 1).getType() instanceof ArrayType) {
					/*
					 * The previous was an ArrayType, so it must be an ArrayType of StructType, we
					 * could also have used the getSimpleType() method, but maybe this is easier
					 */
					StructType typeAux = ((ArrayType) identifiers.get(i - 1).getType()).getComplex();
					/* If it is not an ArrayType of StructType we can go home */
					if (typeAux != null) {
						/* Just repeat the process of the previous case */
						wellIdentified = wellIdentified && checkIdentifiersAux(
								(ArrayList<Declaration>) (typeAux.getDeclarations()), identifiers.get(i));
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		/* The type of a.b. ... .c is the type of c */
		this.type = identifiers.get(identifiers.size() - 1).getType();
		return wellIdentified;
	}

	private boolean checkIdentifiersAux(List<Declaration> declarations, Identifier identifier) {
		for (int i = 0; i < declarations.size(); i++) {
			if (declarations.get(i).getIdentifier().equals(identifier)) {
				/*
				 * Setting the type is necessary, because the identifier is "special", it
				 * escapes from the normal flow of the identifiers
				 */
				identifier.setType(declarations.get(i).getDefinitionType());
				/* Updating offset */
				this.offset += declarations.get(i).getIdentifier().getAddress();
				/* Updating the matched types list */
				this.matchedTypes.add(declarations.get(i).getDefinitionType());
				return true;
			}
		}
		System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
				+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is not defined in the struct");
		return false;
	}

	/* Just returns the types */
	@Override
	public Type getType() {
		return this.type;
	}

	@Override
	public int getRow() {
		return identifiers.get(0).getRow();
	}

	@Override
	public int getColumn() {
		return identifiers.get(0).getColumn();
	}

	public void generateCode(Instructions instructions) {
		instructions.add("lda " + this.deltaDepth + " " + this.address + ";\n");
		//instructions.add("ldc " + this.address + ";\n");
		instructions.add("inc " + offset + ";\n");
		instructions.add("ind;\n");
	}
}
