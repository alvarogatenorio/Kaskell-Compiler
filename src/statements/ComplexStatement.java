package statements;

import kaskell.Block;

/*It must be an abstract class due to the protected modifier doesn't it?*/
public abstract class ComplexStatement implements Statement {
	protected Block body;
}
