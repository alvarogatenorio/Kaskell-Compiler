package functions;

import java.util.List;

import expressions.Identifier;
import types.Type;

public class FunctionHead {
	private Identifier identifier;
	private List<Type> arguments;
	private Type returnType;

	public FunctionHead(Identifier identifier, List<Type> arguments, Type returnType) {
		this.identifier = identifier;
		this.arguments = arguments;
		this.returnType = returnType;
	}

	public List<Type> getArguments() {
		return arguments;
	}

	public Type getReturnType() {
		return returnType;
	}

	public Identifier getIdentifier() {
		return this.identifier;
	}
}
