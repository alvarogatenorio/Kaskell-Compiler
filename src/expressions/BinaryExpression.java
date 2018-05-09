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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return left.checkIdentifiers(symbolTable) && right.checkIdentifiers(symbolTable);
	}
}
