package statements;

import java.util.List;

import expressions.Expression;
import expressions.Identifier;
import kaskell.SymbolTable;
import types.ArrayType;
import types.Type;
import types.Types;

public class ArrayAssignment extends Assignment {
	private List<Expression> coordinates;
	private Type type;

	public ArrayAssignment(Identifier identifier, List<Expression> coordinates, Expression expression) {
		super(identifier, expression);
		this.coordinates = coordinates;
	}

	public boolean checkType() {
		/*
		 * Checks if the right hand side expression type matches with the identifier
		 * simple type
		 */
		if (!expression.checkType()) {
			return false;
		}
		Type identifierType = identifier.getSimpleType();
		if (identifierType != null) {
			if (!identifierType.equals(expression.getType())) {
				System.err.println("TYPE ERROR: in line " + (this.identifier.getRow() + 1) + " column "
						+ (this.identifier.getColumn() + 1)
						+ " the right type of the assignment does not match with the left type of assigment!");
				return false;
			}
		} else {
			return false;
		}
		/*
		 * Checks for each expression in the coordinates to be an integer (there is
		 * always at least one coordinate)
		 */
		if (type instanceof ArrayType && ((ArrayType) type).getSize() == coordinates.size()) {
			for (int i = 0; i < coordinates.size(); i++) {
				if ((!coordinates.get(i).checkType()) || !(coordinates.get(i).getType().equals(new Type(Types.INTEGER)))) {
					System.err.println("TYPE ERROR: in line " + (this.identifier.getRow() + 1) + " column "
							+ (this.identifier.getColumn() + 1)
							+ " fatal error cause by the type of the identifier coordinate!");
					return false;
				}
			}
		} else {
			System.err.println("TYPE ERROR: in line " + (this.identifier.getRow() + 1) + " column "
					+ (this.identifier.getColumn() + 1) + " incongruent dimensions!");
			return false;
		}
		return true;
	}

	/* Checks the identifier and the expressions in the coordinates */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = super.checkIdentifiers(symbolTable);
		type = symbolTable.searchIdentifierType(identifier);
		for (int i = 0; i < coordinates.size(); i++) {
			wellIdentified = wellIdentified && coordinates.get(i).checkIdentifiers(symbolTable);
		}
		return wellIdentified;
	}
}
