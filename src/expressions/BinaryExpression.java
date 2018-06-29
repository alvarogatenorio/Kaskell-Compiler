package expressions;

import kaskell.Instructions;
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

	/* Checks the types of the left hand side and of the right hand side */
	@Override
	public boolean checkType() {
		boolean wellTyped = left.checkType() && right.checkType();
		Type leftType = left.getType();
		Type rightType = right.getType();
		if (leftType == null) {
			System.err.println("TYPE ERROR: in line " + (left.getRow() + 1) + " column " + (left.getColumn() + 1)
					+ " the left type of the expression is void!");
			return false;
		}
		if (rightType == null) {
			System.err.println("TYPE ERROR: in line " + (right.getRow() + 1) + " column " + (right.getColumn() + 1)
					+ " the left type of the expression is void!");
			return false;
		}
		if (left instanceof Identifier) {
			leftType = ((Identifier) left).getSimpleType();
		}
		if (right instanceof Identifier) {
			rightType = ((Identifier) right).getSimpleType();
		}
		/* Equals is an special case */
		if (this.operator == BinaryOperators.EQUALS) {
			this.operator.setEqualsSidesTypes(leftType);
		}
		if (!leftType.equals(operator.getLeftType())) {
			System.err.println("TYPE ERROR: in line " + (this.getRow() + 1) + " column " + (this.getColumn() + 1)
					+ " the left type of the expresion does not match with the left type of the operator!");
			return false;
		}
		if (!rightType.equals(operator.getRightType())) {
			System.err.println("TYPE ERROR: in line " + (this.getRow() + 1) + " column " + (this.getColumn() + 1)
					+ " the right type of the expresion does not match with the right type of the operator!");
			return false;
		}

		return wellTyped;
	}

	/* Returns the type of the expression, is never null */
	@Override
	public Type getType() {
		return operator.getType();
	}

	/* Checks identifiers of the left and right hand side */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return left.checkIdentifiers(symbolTable) && right.checkIdentifiers(symbolTable);
	}

	@Override
	public int getRow() {
		return this.left.getRow();
	}

	@Override
	public int getColumn() {
		return this.left.getColumn();
	}

	public void generateCode(Instructions instructions) {
		/* Generate code for the operator */
		switch (operator) {
		case AND:
			left.generateCode(instructions);
			right.generateCode(instructions);
			instructions.add("and;\n");
			break;
		case DIV:
			left.generateCode(instructions);
			right.generateCode(instructions);
			instructions.add("div;\n");
			break;
		case EQUALS:
			left.generateCode(instructions);
			right.generateCode(instructions);
			instructions.add("equ;\n");
			break;
		case GREATER:
			left.generateCode(instructions);
			right.generateCode(instructions);
			instructions.add("grt;\n");
			break;
		case LOWER:
			left.generateCode(instructions);
			right.generateCode(instructions);
			instructions.add("les;\n");
			break;
		case MINUS:
			left.generateCode(instructions);
			right.generateCode(instructions);
			instructions.add("sub;\n");
			break;
		case OR:
			left.generateCode(instructions);
			right.generateCode(instructions);
			instructions.add("or;\n");
			break;
		case PLUS:
			left.generateCode(instructions);
			right.generateCode(instructions);
			instructions.add("add;\n");
			break;
		case PRODUCT:
			left.generateCode(instructions);
			right.generateCode(instructions);
			instructions.add("mul;\n");
			break;
		/*-----Non simple cases-----*/
		case EXPONENTIAL:
			// STILL A PARTY
			break;
		case MODULUS:
			BinaryExpression q = new BinaryExpression(left, BinaryOperators.DIV, right);
			BinaryExpression dq = new BinaryExpression(right, BinaryOperators.PRODUCT, q);
			BinaryExpression aux = new BinaryExpression(left, BinaryOperators.MINUS, dq);
			aux.generateCode(instructions);
			break;
		default:
			break;
		}
	}
}
