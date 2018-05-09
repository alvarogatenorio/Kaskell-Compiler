package expressions;

import kaskell.SymbolTable;
import types.Type;

public interface Expression {
	public boolean checkType();
	public boolean checkIdentifiers(SymbolTable symbolTable);
	public Type getType();
	
}
