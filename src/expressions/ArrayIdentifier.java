package expressions;

import java.util.List;

import kaskell.SymbolTable;
import types.ArrayType;
import types.Type;
import types.Types;

public class ArrayIdentifier extends Identifier {
	private List<Expression> coordinates;

	public ArrayIdentifier(String s, List<Expression> coordinates) {
		super(s);
		this.coordinates = coordinates;
	}

	/*
	 * Checks if the identifier is well identified and also checks the list of
	 * coordinates
	 */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = super.checkIdentifiers(symbolTable);
		for (int i = 0; i < coordinates.size(); i++) {
			wellIdentified = wellIdentified && coordinates.get(i).checkIdentifiers(symbolTable);
		}
		return wellIdentified;
	}

	/*
	 * Checks if the coordinates number matches with the original type, and then
	 * checks if all the expressions in the coordinates are well typed and integer
	 */
	public boolean checkType() {
		boolean wellTyped = true;
		ArrayType type = (ArrayType) (this.getType());
		if (type.getSize() == coordinates.size()) {
			for (int i = 0; i < coordinates.size(); i++) {
				wellTyped = wellTyped && coordinates.get(i).checkType();
				if (!(coordinates.get(i).getType().equals(new Type(Types.INTEGER)))) {
					System.err.println("TYPE ERROR: in line " + (coordinates.get(i).getRow() + 1) + " column "
							+ (coordinates.get(i).getColumn() + 1)
							+ " the expression must be integer!");
					return false;
				}
			}
		} else {
			System.err.println("TYPE ERROR: in line " + (this.getRow() + 1) + " column " + (this.getColumn() + 1)
					+ " the number of coordinates dosen't match with the number in the original declaration!");
			return false;
		}
		return wellTyped;
	}

	public int getSize() {
		return this.coordinates.size();
	}
}
