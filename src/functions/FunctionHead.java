package functions;

import java.util.List;

import expressions.Identifier;
import kaskell.SymbolTable;
import types.StructType;
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

	public boolean checkIdentifiers(SymbolTable symbolTable) {
		if (arguments != null) {
			for (int i = 0; i < arguments.size(); i++) {
				if (arguments.get(i) instanceof StructType) {
					Identifier id = ((StructType) (arguments.get(i))).getIdentifier();
					Type type = symbolTable.searchStructType(id);
					if (type != null) {
						arguments.set(i, type);
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}
}
