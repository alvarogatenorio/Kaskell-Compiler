package kaskell;

import java.util.List;

import statements.Statement;

public class Block implements Statement {
	private List<Statement> statements;

	public Block(List<Statement> statements) {
		this.statements = statements;
	}
}
