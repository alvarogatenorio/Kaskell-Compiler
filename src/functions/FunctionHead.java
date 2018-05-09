package functions;

import java.util.List;

import expressions.Identifier;
import kaskell.Definition;
import kaskell.SymbolTable;
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

	public Identifier getIdentifier() {
		return this.identifier;
	}
}
