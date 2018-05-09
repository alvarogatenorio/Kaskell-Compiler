package expressions;

import types.Type;
import types.Types;

public enum UnaryOperators {
	UNARY_MINUS(new Type(Types.INTEGER)), PLUS_PLUS(new Type(Types.INTEGER)), MINUS_MINUS(new Type(Types.INTEGER)), NOT(
			new Type(Types.BOOLEAN));
	private Type type;

	private UnaryOperators(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}
}
