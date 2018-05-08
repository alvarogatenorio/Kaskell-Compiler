package statements;

import expressions.Expression;
import kaskell.Block;

public class If extends ComplexStatement {
	protected Expression condition;

	public If(Expression condition, Block thenBody) {
		this.condition = condition;
		this.body = thenBody;
	}
}
