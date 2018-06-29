package kaskell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import expressions.Identifier;
import functions.FunctionTail;
import statements.Declaration;
import statements.Mixed;
import types.StructType;
import types.Type;

/*All the identifier error management is done here*/
public class SymbolTable {

	/*
	 * The list will always have at least three hash maps, the first one for the
	 * StructTypes identifiers, the second one for the functions identifiers, and
	 * the rest for the visible blocks at each moment
	 */
	private List<HashMap<String, Definition>> table;
	private List<Integer> accumulator;

	public SymbolTable() {
		table = new ArrayList<HashMap<String, Definition>>();
		accumulator = new ArrayList<Integer>();
	}

	/*-----Basic operations-----*/

	/*
	 * Adds a new hash map to the list and its corresponding accumulator for
	 * computing addresses
	 */
	public void startBlock() {
		table.add(new HashMap<String, Definition>());
		accumulator.add(0);
	}

	/* Removes the last hash map and its corresponding accumulator */
	public void closeBlock() {
		table.remove(table.size() - 1);
		accumulator.remove(accumulator.size() - 1);
	}

	public int getDepth() {
		return table.size() - 1;
	}

	/*
	 * Tries to insert an identifier in the last hash map and updates the
	 * accumulator
	 */
	public boolean insertIdentifier(Identifier identifier, Definition definition) {
		/* Return false if the identifier was already defined */
		boolean wellIdentified = table.get(table.size() - 1).put(identifier.toString(), definition) == null ? true
				: false;
		if (!wellIdentified) {
			System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
					+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is duplicated in this scope");
		} else {
			/* Updates the accumulation */
			if (definition instanceof Declaration) { // declaration
				accumulator.set(accumulator.size() - 1,
						accumulator.get(accumulator.size() - 1) + ((Declaration) (definition)).getSize());
			} else if (definition instanceof Mixed) { // mixed
				accumulator.set(accumulator.size() - 1,
						accumulator.get(accumulator.size() - 1) + ((Mixed) (definition)).getSize());
			}
		}
		return wellIdentified;
	}

	/* Gets the value of the last accumulator */
	public int getAccumulation() {
		return this.accumulator.get(this.accumulator.size() - 1);
	}

	/*-----Weird operations-----*/

	public Type searchStructFieldType(Identifier field) {
		return table.get(1).get(field.toString()).getDefinitionType();
	}

	public List<Type> searchCallArguments(Identifier call) {
		FunctionTail tail = this.searchFunctionTailFromCall(call);
		if (tail != null) {
			return tail.getArguments();
		}
		System.err.println("IDENTIFIER ERROR: line " + (call.getRow() + 1) + " column " + (call.getColumn() + 1) + ", "
				+ call.toString() + " is not defined in this scope");
		return null;
	}

	public Type searchCallType(Identifier call) {
		FunctionTail tail = this.searchFunctionTailFromCall(call);
		if (tail != null) {
			return tail.getDefinitionType();
		}
		System.err.println("IDENTIFIER ERROR: line " + (call.getRow() + 1) + " column " + (call.getColumn() + 1) + ", "
				+ call.toString() + " is not defined in this scope");
		return null;
	}

	/*
	 * Returns the type of the parameter identifier if it is visible in the symbol
	 * table, if not, it returns null
	 */
	public Type searchIdentifierType(Identifier identifier) {
		Definition definition = this.searchDefinitionFromIdentifier(identifier);
		if (definition != null) {
			if (definition instanceof FunctionTail) {
				/*
				 * This will have a better performance if we previously insert every variable in
				 * a hashmap (to do in the future)
				 */
				List<Identifier> aux = ((FunctionTail) definition).getVariables();
				for (int i = 0; i < aux.size(); i++) {
					if (aux.get(i).toString().equals(identifier.toString())) {
						return aux.get(i).getType();
					}
				}
			}
			return definition.getDefinitionType();
		}
		System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
				+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is not defined in this scope");
		return null;
	}

	/*
	 * Returns the StructType of the parameter identifier, which is supposed to be
	 * an StructType identifier, so stored in the first hash map of the list
	 */
	public Type searchStructType(Identifier identifier) {
		StructType type = (StructType) (table.get(0).get(identifier.toString()));
		if (type != null) {
			return type;
		}
		System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
				+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is not defined in this scope");
		return null;
	}

	/*-----Auxiliary functions-----*/

	/* Returns the definition of an identifier, if not defined, returns null */
	private Definition searchDefinitionFromIdentifier(Identifier identifier) {
		Definition definition = null;
		/* Don't look at the first two levels, reserved for StructTypes and functions */
		for (int i = table.size() - 1; i > 1; i--) {
			definition = table.get(i).get(identifier.toString());
			if (definition != null) {
				return definition;
			}
		}
		return definition;
	}

	public FunctionTail searchFunctionTailFromCall(Identifier call) {
		return (FunctionTail) (table.get(1).get(call.toString()));
	}

	/*-----These last functions are commonly used-----*/

	/* With level >1!! */
	public boolean searchIdentifier(Identifier identifier) {
		/* Don't look at the first two levels! */
		if (this.searchDefinitionFromIdentifier(identifier) != null) {
			return true;
		}
		System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
				+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is not defined in this scope");
		return false;
	}

	/* With level 1!! */
	public boolean searchFunctionIdentifier(Identifier identifier) {
		boolean wellIdentified = table.get(1).get(identifier.toString()) != null;
		if (!wellIdentified) {
			System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
					+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is not defined in this scope");
		}
		return wellIdentified;
	}

	/* With level 0!! */
	public boolean searchStructIdentifier(Identifier identifier) {
		boolean wellIdentified = table.get(0).get(identifier.toString()) != null;
		if (!wellIdentified) {
			System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
					+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is not defined in this scope");
		}
		return wellIdentified;
	}

	public int searchIdentifierAddress(Identifier identifier) {
		/* First we get the definition from the identifier */
		Definition def = searchDefinitionFromIdentifier(identifier);
		/* Then we get the actual address */
		if (def instanceof Declaration) {
			return ((Declaration) def).getAddress(this);
		} else if (def instanceof Mixed) {
			return ((Mixed) def).getAddress();
		}
		/*
		 * Be careful, it returns -1 when the identifier is not induced by a Declaration
		 * or a Mixed
		 */
		return -1;
	}

	public int searchDefinitionDepth(Identifier identifier) {
		/* Don't look at the first two levels, reserved for StructTypes and functions */
		for (int i = table.size() - 1; i > 1; i--) {
			Definition definition = table.get(i).get(identifier.toString());
			if (definition != null) {
				return i;
			}
		}
		/* This will never reach this point */
		return -1;
	}
}
