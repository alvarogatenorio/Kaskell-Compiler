package expressions;

import java.util.List;

import kaskell.SymbolTable;
import types.Type;

public class Call implements Expression {
	private Identifier identifier;
	private List<Expression> arguments;

	public Call(Identifier identifier, List<Expression> arguments) {
		this.identifier = identifier;
		this.arguments = arguments;
	}

	@Override
	public boolean checkType() {
		return false;
	}

	@Override
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = symbolTable.searchGlocalIdentifier(identifier);
		if (arguments != null) {
			for (int i = 0; i < arguments.size(); i++) {
				wellIdentified = wellIdentified && arguments.get(i).checkIdentifiers(symbolTable);
			}
		}
		return wellIdentified;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}
}
