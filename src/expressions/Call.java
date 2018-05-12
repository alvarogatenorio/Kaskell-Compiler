package expressions;

import java.util.List;

import kaskell.SymbolTable;
import types.Type;

public class Call implements Expression {
	private Identifier identifier;
	private List<Expression> arguments;
	private Type type;

	public Call(Identifier identifier, List<Expression> arguments) {
		this.identifier = identifier;
		this.arguments = arguments;
	}

	@Override
	public boolean checkType() {
		return true;
	}

	/*
	 * Checks if the function identifier is defined, and also checks if the list of
	 * expressions (arguments) is well identified
	 */
	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = symbolTable.searchGlocalIdentifier(identifier);
		if (arguments != null) {
			for (int i = 0; i < arguments.size(); i++) {
				wellIdentified = wellIdentified && arguments.get(i).checkIdentifiers(symbolTable);
			}
		}
		if (wellIdentified && (type == null)) {
			type = symbolTable.searchCallType(identifier);
		}
		return wellIdentified;
	}

	@Override
	public Type getType() {
		return type;
	}
}
