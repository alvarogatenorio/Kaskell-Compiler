package statements;

import expressions.Expression;
import expressions.Identifier;
import types.Type;

public class Mixed extends BasicStatement {
	private Type type;
	private Expression expression;

	public Mixed(Type type, Identifier identifier, Expression expression) {
		this.type = type;
		this.identifier = identifier;
		this.expression = expression;
	}
}
