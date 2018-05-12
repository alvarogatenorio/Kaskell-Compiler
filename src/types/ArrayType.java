package types;

import java.util.List;

public class ArrayType extends Type {

	private List<Integer> dimensions;

	public ArrayType(Types type, List<Integer> dimensions) {
		super(type);
		this.dimensions = dimensions;
	}

	public int getSize() {
		return this.dimensions.size();
	}

	/*
	 * We consider two arrays equals in terms of types if both have the same
	 * dimension size and have the same basic type
	 */
	public boolean equals(Type other) {
		if (other instanceof ArrayType) {
			ArrayType aux = (ArrayType) other;
			if ((other.getType() == this.getType()) && (aux.getSize() == this.getSize())) {
				return true;
			}
		}
		return false;
	}
}
