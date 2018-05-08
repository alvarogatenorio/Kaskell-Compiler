package expressions;

public class UnaryExpression implements Expression {
	private UnaryOperators operator;
	private Expression expression;

	public UnaryExpression(UnaryOperators operator, Expression expression) {
		this.operator = operator;
		this.expression = expression;
	}
}
