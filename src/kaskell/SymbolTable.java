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
	private List<HashMap<String, Definition>> table;
	private List<Integer> accumulator;

	public SymbolTable() {
		table = new ArrayList<HashMap<String, Definition>>();
		accumulator = new ArrayList<Integer>();
	}

	/*-----Basic operations-----*/

	public void startBlock() {
		table.add(new HashMap<String, Definition>());
		accumulator.add(0);
	}

	public void closeBlock() {
		table.remove(table.size() - 1);
		accumulator.remove(accumulator.size() - 1);
	}

	public boolean insertIdentifier(Identifier identifier, Definition definition) {
		/* Return false if the identifier was already defined */
		boolean wellIdentified = table.get(table.size() - 1).put(identifier.toString(), definition) == null ? true
				: false;
		if (!wellIdentified) {
			System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
					+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is duplicated in this scope");
		}
		/* Updates the accumulation */
		if (definition instanceof Declaration) {
			accumulator.set(accumulator.size() - 1,
					accumulator.get(accumulator.size() - 1) + ((Declaration) (definition)).getSize());
		} else if (definition instanceof Mixed) {
			accumulator.set(accumulator.size() - 1,
					accumulator.get(accumulator.size() - 1) + ((Mixed) (definition)).getSize());
		}
		return wellIdentified;
	}

	public int getAccumulation() {
		return this.accumulator.get(this.accumulator.size() - 1);
	}

	/*-----Weird operations-----*/
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

	/* Returns the definition of an identifier, id not defined, returns null */
	private Definition searchDefinitionFromIdentifier(Identifier identifier) {
		Definition definition = null;
		/* Don't look at the first two levels! */
		for (int i = table.size() - 1; i > 1; i--) {
			definition = table.get(i).get(identifier.toString());
			if (definition != null) {
				return definition;
			}
		}
		return definition;
	}

	private FunctionTail searchFunctionTailFromCall(Identifier call) {
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
}
