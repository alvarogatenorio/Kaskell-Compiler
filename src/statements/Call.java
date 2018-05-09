package statements;

import java.util.List;

import expressions.Identifier;
import kaskell.SymbolTable;

public class Call extends BasicStatement {
	private List<Identifier> arguments;

	public Call(Identifier identifier, List<Identifier> arguments) {
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
}
