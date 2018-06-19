package expressions;

import java.io.BufferedWriter;

import kaskell.SymbolTable;
import types.Type;

public class UnaryExpression implements Expression {
	private UnaryOperators operator;
	private Expression expression;

	public UnaryExpression(UnaryOperators operator, Expression expression) {
		this.operator = operator;
		this.expression = expression;
	}

	/*
	 * Checks the expression and if the type of the expression equals the type of
	 * the operator
	 */
	@Override
	public boolean checkType() {
		boolean wellTyped = expression.checkType();
		Type type = expression.getType();
		if (expression instanceof Identifier) {
			type = ((Identifier) expression).getSimpleType();
		}
		
		if(!type.equals(operator.getType())) {
			System.err.println("TYPE ERROR: in line " + (this.getRow() + 1) + " column "
					+ (this.getColumn() + 1)
					+ " the type of the expresion does not match with the type of the operator!");
			return false;
		}
		
		return wellTyped && (type.equals(operator.getType()));
	}

	/* Just checks the expression */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return expression.checkIdentifiers(symbolTable);
	}

	@Override
	public Type getType() {
		return operator.getType();
	}

	@Override
	public int getRow() {
		return this.expression.getRow();
	}

	@Override
	public int getColumn() {
		return this.expression.getColumn();
	}

	@Override
	public void generateCode(BufferedWriter bw) {
		// TODO Auto-generated method stub
		
	}
}
