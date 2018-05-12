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
		/*
		 * Checks if the right hand side expression type matches with the identifier
		 * simple type
		 */
		boolean wellTyped = expression.checkType() && (identifier.getSimpleType().equals(expression.getType()));
		/* Checks the identifier's type */
		wellTyped = wellTyped && identifier.checkType();
		/*
		 * Checks for each expression in the coordinates to be an integer (there is
		 * always at least one coordinate)
		 */
		for (int i = 0; i < coordinates.size(); i++) {
			wellTyped = wellTyped && (coordinates.get(i).checkType()
					&& (coordinates.get(i).getType().equals(new Type(Types.INTEGER))));
		}
		return wellTyped;
	}

	/* Checks the identifier and the expressions in the coordinates */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = super.checkIdentifiers(symbolTable);
		for (int i = 0; i < coordinates.size(); i++) {
			wellIdentified = wellIdentified && coordinates.get(i).checkIdentifiers(symbolTable);
		}
		return wellIdentified;
	}
}
