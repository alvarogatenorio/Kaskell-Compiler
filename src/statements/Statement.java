package statements;

import java.io.BufferedWriter;

import kaskell.SymbolTable;

public interface Statement {
	public boolean checkType();

	public boolean checkIdentifiers(SymbolTable symbolTable);

	public void generateCode(BufferedWriter bw) throws Exception;
}
