package statements;

import expressions.Identifier;
import kaskell.Definition;
import kaskell.SymbolTable;

public abstract class BasicStatement extends Definition implements Statement {
	protected Identifier identifier;
}
