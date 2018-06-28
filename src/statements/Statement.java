package statements;

import kaskell.Instructions;
import kaskell.SymbolTable;

public interface Statement {
	public boolean checkType();

	public boolean checkIdentifiers(SymbolTable symbolTable);

	public void generateCode(Instructions instructions);
}
