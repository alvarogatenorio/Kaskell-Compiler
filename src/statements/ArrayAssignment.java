package statements;

import java.util.List;

import expressions.Expression;
import expressions.Identifier;
import kaskell.SymbolTable;
import types.Type;
import types.Types;

public class ArrayAssignment extends Assignment {
	private List<Expression> coordinates;

	public ArrayAssignment(Identifier identifier, List<Expression> coordinates, Expression expression) {
		super(identifier, expression);
		this.coordinates = coordinates;
	}

	public boolean checkType() {
		boolean wellTyped = expression.checkType() && (identifier.getSimpleType() == expression.getType());
		for (int i = 0; i < coordinates.size(); i++) {
			wellTyped = wellTyped
					&& (coordinates.get(i).checkType() && (coordinates.get(i).getType() == new Type(Types.INTEGER)));
		}
		return wellTyped;
	}

	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = super.checkIdentifiers(symbolTable);
		for (int i = 0; i < coordinates.size(); i++) {
			wellIdentified = wellIdentified && coordinates.get(i).checkIdentifiers(symbolTable);
		}
		return wellIdentified;
	}
}
