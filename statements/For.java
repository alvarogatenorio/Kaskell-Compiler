package statements;

import kaskell.Block;

public class For extends ComplexStatement {
	private ForTuple conditions;

	public For(ForTuple conditions, Block body) {
		this.conditions = conditions;
		this.body = body;
	}
}
