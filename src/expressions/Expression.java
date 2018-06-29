package expressions;

import kaskell.Instructions;
import statements.BasicStatement;
import types.Type;

public interface Expression extends BasicStatement {
	/* Invariant: After checking identifiers, each expression has a non null type */

	public Type getType();

	public int getRow();

	public int getColumn();

	public void generateCode(Instructions instructions);
	
}
