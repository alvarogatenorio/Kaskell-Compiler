package expressions;

import kaskell.Instructions;
import kaskell.SymbolTable;
import statements.Assignment;
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
	 * the operator. Be careful with the ++ and -- operators, which are only well
	 * typed if the expression is an identifier
	 */
	@Override
	public boolean checkType() {
		/* First we check the type of the expression */
		boolean wellTyped = expression.checkType();
		Type type = expression.getType();
		if (expression instanceof Identifier) {
			type = ((Identifier) expression).getSimpleType();
		}
		if (!type.equals(operator.getType())) {
			System.err.println("TYPE ERROR: in line " + (this.getRow() + 1) + " column " + (this.getColumn() + 1)
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

	/* Returns the expression type, is never null */
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

	public void generateCode(Instructions instructions) {
		switch (operator) {
		case NOT:
			expression.generateCode(instructions);
			instructions.add("not;\n");
			break;
		case UNARY_MINUS:
			expression.generateCode(instructions);
			instructions.add("neg;\n");
			break;
		/*-----Non simple cases-----*/
		case MINUS_MINUS:
			if (expression instanceof Identifier) {
				Assignment aux = new Assignment((Identifier) expression,
						new BinaryExpression(expression, BinaryOperators.MINUS, new DummyInteger(1)));
				aux.generateCode(instructions);
			} else {
				Expression aux = new BinaryExpression(expression, BinaryOperators.MINUS, new DummyInteger(1));
				aux.generateCode(instructions);
			}
			break;
		case PLUS_PLUS:
			if (expression instanceof Identifier) {
				Assignment aux = new Assignment((Identifier) expression,
						new BinaryExpression(expression, BinaryOperators.PLUS, new DummyInteger(1)));
				aux.generateCode(instructions);
			} else {

				Expression aux = new BinaryExpression(expression, BinaryOperators.PLUS, new DummyInteger(1));
				aux.generateCode(instructions);
			}
			break;
		default:
			break;
		}
	}
}
