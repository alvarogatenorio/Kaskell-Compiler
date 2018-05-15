package expressions;

import types.Type;
import types.Types;

public enum BinaryOperators {
	AND(new Type(Types.BOOLEAN)), DIV(new Type(Types.INTEGER)), EQUALS(null, new Type(Types.BOOLEAN),
			null), EXPONENTIAL(new Type(Types.INTEGER)), GREATER(new Type(Types.INTEGER), new Type(Types.BOOLEAN),
					new Type(Types.INTEGER)), LOWER(new Type(Types.INTEGER), new Type(Types.BOOLEAN),
							new Type(Types.INTEGER)), MINUS(new Type(Types.INTEGER)), MODULUS(
									new Type(Types.INTEGER)), OR(new Type(Types.BOOLEAN)), PLUS(
											new Type(Types.INTEGER)), PRODUCT(new Type(Types.INTEGER));
	private Type type;
	private Type left;
	private Type right;

	private BinaryOperators(Type left, Type op, Type right) {
		this.left = left;
		this.type = op;
		this.right = right;
	}

	private BinaryOperators(Type op) {
		this.left = op;
		this.type = op;
		this.right = op;
	}

	public Type getType() {
		return this.type;
	}

	public Type getLeftType() {
		return this.left;
	}

	public Type getRightType() {
		return this.right;
	}
	
	public void setEqualsSidesTypes(Type types) {
		this.left = types;
		this.right = types;
	}
}
