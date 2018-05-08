package statements;

import expressions.Expression;
import kaskell.Block;

public class While extends ComplexStatement {
	private Expression condition;

	public While(Expression condition, Block body) {
		this.condition = condition;
		this.body = body;
	}
}
