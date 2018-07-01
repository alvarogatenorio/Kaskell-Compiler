package kaskell;

import java.util.List;

import expressions.Expression;
import expressions.Identifier;
import functions.FunctionTail;
import statements.Assignment;
import statements.Declaration;
import statements.Statement;

public class ReturnBlock extends Block {
	private Expression returnStatement;
	private Identifier id;
	private Assignment aux;
	private int returnAddress;
	private Declaration decAux;

	public ReturnBlock(List<Statement> statements, Expression returnStatement) {
		super(statements);
		this.returnStatement = returnStatement;
		this.id = new Identifier("1");
		this.decAux = new Declaration(returnStatement.getType(), id);
		this.aux = new Assignment(id, returnStatement);
	}

	/* Checks the block itself and the expression (return statement) */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = super.checkIdentifiers(symbolTable);
		wellIdentified = wellIdentified && decAux.checkIdentifiers(symbolTable) && aux.checkIdentifiers(symbolTable);
		this.returnAddress = id.getAddress();
		return wellIdentified;
	}

	/* Checks the block itself and the expression (return statement) */
	public boolean checkType() {
		return super.checkType() && aux.checkType();
	}

	public int lengthStackExpressions() {
		return Math.max(super.lengthStackExpressions(), this.calculateExpSubTree(returnStatement));
	}

	public void setInsideFunction(FunctionTail f) {
		super.setInsideFunction(f);
		id.setFunctionInside(f);
		id.setInsideFunction(true);
		returnStatement.setFunctionInside(f);
		returnStatement.setInsideFunction(true);
	}

	public Expression getReturnExpression() {
		return returnStatement;
	}

	public void generateCode(Instructions instructions) {
		super.generateCode(instructions);
		aux.generateCode(instructions);
	}

	public int getAddress() {
		return this.returnAddress;
	}
}
