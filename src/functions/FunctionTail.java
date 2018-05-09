package functions;

import java.util.List;

import expressions.Identifier;
import kaskell.Block;
import kaskell.Definition;
import kaskell.SymbolTable;

public class FunctionTail extends Definition {
	private List<Identifier> variables;
	private Block body;

	public FunctionTail(List<Identifier> variables, Block body) {
		this.variables = variables;
		this.body = body;
	}

	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = true;
		if (variables != null) {
			for (int i = 0; i < variables.size(); i++) {
				wellIdentified = wellIdentified && symbolTable.insertIdentifier(variables.get(i), this);
			}
		}
		if (wellIdentified) {
			wellIdentified = body.checkIdentifiers(symbolTable);
		}
		return wellIdentified;
	}
}
