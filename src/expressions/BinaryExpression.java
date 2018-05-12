package expressions;

import kaskell.SymbolTable;
import types.Type;

public class BinaryExpression implements Expression {
	private BinaryOperators operator;
	private Expression left;
	private Expression right;

	public BinaryExpression(Expression left, BinaryOperators operator, Expression right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}
	
	/*Checks the types of the left hand side and of the right hand side*/
	@Override
	public boolean checkType() {
		return left.checkType() && right.checkType() && (left.getType().equals(operator.getLeftType()))
				&& (right.getType().equals(operator.getRightType()));
	}
	
	@Override
	public Type getType() {
		return operator.getType();
	}

	/* Checks identifiers of the left and right hand side */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return left.checkIdentifiers(symbolTable) && right.checkIdentifiers(symbolTable);
	}
}
