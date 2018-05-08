package statements;

import expressions.Expression;
import expressions.Identifier;

public class Assignment extends BasicStatement {
	protected Expression expression;

	public Assignment(Identifier identifier, Expression expression) {
		this.identifier = identifier;
		this.expression = expression;
	}
}
