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

	@Override
	public boolean checkType() {
		return left.checkType() && right.checkType() && (left.getType() == operator.getLeftType())
				&& (right.getType() == operator.getRightType());
	}

	@Override
	public Type getType() {
		return operator.getType();
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return left.checkIdentifiers(symbolTable) && right.checkIdentifiers(symbolTable);
	}
}
