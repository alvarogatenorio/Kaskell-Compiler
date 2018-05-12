package kaskell;

import java.util.List;

import expressions.Expression;
import statements.Statement;

public class ReturnBlock extends Block {
	private Expression returnStatement;

	public ReturnBlock(List<Statement> statements, Expression returnStatement) {
		super(statements);
		this.returnStatement = returnStatement;
	}

	/* Checks the block itself and the expression (return statement) */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return super.checkIdentifiers(symbolTable) && returnStatement.checkIdentifiers(symbolTable);
	}

	/* Checks the block itself and the expression (return statement) */
	public boolean checkType() {
		return super.checkType() && returnStatement.checkType();
	}

	public Expression getReturnExpression() {
		return returnStatement;
	}
}
