package statements;

import expressions.Identifier;
import types.Type;

public class Declaration extends BasicStatement {
	private Type type;
	private Identifier identifier;

	public Declaration(Type type, Identifier identifier) {
		this.type = type;
		this.identifier = identifier;
	}
}
