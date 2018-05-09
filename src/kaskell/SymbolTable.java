package kaskell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import expressions.Identifier;

public class SymbolTable {
	private List<HashMap<Identifier, Definition>> table;

	public SymbolTable() {
		table = new ArrayList<HashMap<Identifier, Definition>>();
	}

	public void startBlock() {
		table.add(new HashMap<Identifier, Definition>());
	}

	public void closeBlock() {
		table.remove(table.size() - 1);
	}

	public boolean insertIdentifier(Identifier identifier, Definition definition) {
		/* Return false if the identifier was already defined */
		return table.get(table.size() - 1).put(identifier, definition) == null ? true : false;
	}

	public boolean searchIdentifier(Identifier identifier) {
		/* Don't look at the first level! */
		for (int i = table.size() - 1; i > 0; i--) {
			if (table.get(i).get(identifier) != null) {
				return true;
			}
		}
		System.err.println("IDENTIFIER ERROR: line " + (identifier.getRow() + 1) + " column "
				+ (identifier.getColumn() + 1) + ", " + identifier.toString() + " is not defined in this scope");
		return false;
	}

	public boolean searchGlocalIdentifier(Identifier identifier) {
		return table.get(0).get(identifier) != null;
	}
}
