package kaskell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import expressions.Identifier;
import functions.FunctionTail;
import types.Type;

/*All the identifier error management is done here*/
public class SymbolTable {
	private List<HashMap<String, Definition>> table;

	public SymbolTable() {
		table = new ArrayList<HashMap<String, Definition>>();
	}

	public void startBlock() {
		table.add(new HashMap<String, Definition>());
	}

	public void closeBlock() {
		table.remove(table.size() - 1);
	}

	public boolean insertIdentifier(Identifier identifier, Definition definition) {
		/* Return false if the identifier was already defined */
		boolean wellIdentified = table.get(table.size() - 1).put(identifier.toString(), definition) == null ? true
				: false;
		if (!wellIdentified) {
			System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
					+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is duplicated in this scope");
		}
		return wellIdentified;
	}

	/* Returns the definition of an identifier, id not defined, returns null */
	private Definition searchDefinitionIdentifier(Identifier identifier) {
		Definition definition = null;
		/* Don't look at the first level! */
		for (int i = table.size() - 1; i > 0; i--) {
			definition = table.get(i).get(identifier.toString());
			if (definition != null) {
				return definition;
			}
		}
		return definition;
	}

	private FunctionTail searchFunctionTailCall(Identifier call) {
		return (FunctionTail) (table.get(0).get(call.toString()));
	}

	public Type searchCallType(Identifier call) {
		FunctionTail tail = this.searchFunctionTailCall(call);
		if (tail != null) {
			return tail.getType();
		}
		return null;
	}

	public boolean searchIdentifier(Identifier identifier) {
		/* Don't look at the first level! */
		if (this.searchDefinitionIdentifier(identifier) != null) {
			return true;
		}
		System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
				+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is not defined in this scope");
		return false;
	}

	public boolean searchGlocalIdentifier(Identifier identifier) {
		boolean wellIdentified = table.get(0).get(identifier.toString()) != null;
		if (!wellIdentified) {
			System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
					+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is not defined in this scope");
		}
		return wellIdentified;
	}

	public Type searchIdentifierType(Identifier identifier) {
		Definition definition = this.searchDefinitionIdentifier(identifier);
		if (definition != null) {
			return definition.getType();
		}
		return null;
	}
}
