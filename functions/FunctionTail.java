package functions;

import java.util.List;

import expressions.Identifier;
import kaskell.ReturnBlock;

public class FunctionTail {
	private List<Identifier> variables;
	private ReturnBlock body;

	public FunctionTail(List<Identifier> variables, ReturnBlock body) {
		this.variables = variables;
		this.body = body;
	}
}
