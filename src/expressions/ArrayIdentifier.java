package expressions;

import java.util.ArrayList;
import java.util.List;

import kaskell.Instructions;
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
		/*
		 * Calls to its super, so here the ArrayIdentifier gets its type and its
		 * address, notice it will be the address of the beginning of the array, it will
		 * be modified later
		 */
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
		if (type.getDimensions().size() == coordinates.size()) {
			for (int i = 0; i < coordinates.size(); i++) {
				wellTyped = wellTyped && coordinates.get(i).checkType();
				if (!(coordinates.get(i).getType().equals(new Type(Types.INTEGER)))) {
					System.err.println("TYPE ERROR: in line " + (coordinates.get(i).getRow() + 1) + " column "
							+ (coordinates.get(i).getColumn() + 1) + " the expression must be integer!");
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

	/* Just follows the scheme, first compute the auxiliary quantities */
	@Override
	public void generateCode(Instructions instructions) {
		/* Computing auxiliary stuff */
		List<Integer> dimensions = ((ArrayType) (this.getType())).getDimensions();
		int g = this.getSimpleType().getSize();
		/* Initializing the list (probably there is a better way) */
		List<Integer> d = new ArrayList<Integer>();
		for (int i = 0; i < dimensions.size(); i++) {
			d.add(0);
		}
		d.set(d.size() - 1, 1);
		for (int i = dimensions.size() - 2; i >= 0; i--) {
			d.set(i, (dimensions.get(i + 1) + 1) * d.get(i + 1));
		}
		/* Generating actual code */
		// instructions.add("ldc " + this.address + ";\n");
		instructions.add("lda " + this.deltaDepth + " " + this.address + ";\n");
		for (int i = 0; i < coordinates.size(); i++) {
			/* Generates code for coordinate expression */
			coordinates.get(i).generateCode(instructions);
			/* Our lower bound is always 0 */
			instructions.add("chk 0 " + dimensions.get(i) + ";\n");
			instructions.add("ixa " + g * d.get(i) + ";\n");
		}
		instructions.add("ind;\n");
	}

	public List<Expression> getCoordinates() {
		return this.coordinates;
	}
}
