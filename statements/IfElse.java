package statements;

import expressions.Expression;
import kaskell.Block;

public class IfElse extends If {
	private Block elseBody;

	public IfElse(Expression condition, Block thenBody, Block elseBody) {
		super(condition, thenBody);
		this.elseBody = elseBody;
	}
}
