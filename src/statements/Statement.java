package statements;

import kaskell.SymbolTable;

public interface Statement {
	public boolean checkType();

	public boolean checkIdentifiers(SymbolTable symbolTable);
}
