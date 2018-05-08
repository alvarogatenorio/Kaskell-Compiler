package statements;

import java.util.List;

import expressions.Identifier;

public class Call extends BasicStatement {
	private List<Identifier> arguments;

	public Call(Identifier identifier, List<Identifier> arguments) {
		this.identifier = identifier;
		this.arguments = arguments;
	}
}
