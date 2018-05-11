package expressions;

import kaskell.SymbolTable;
import types.Type;

public class UnaryExpression implements Expression {
	private UnaryOperators operator;
	private Expression expression;

	public UnaryExpression(UnaryOperators operator, Expression expression) {
		this.operator = operator;
		this.expression = expression;
	}

	@Override
	public boolean checkType() {
		return expression.getType() == operator.getType();
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return expression.checkIdentifiers(symbolTable);
	}

	@Override
	public Type getType() {
		return operator.getType();
	}
}
