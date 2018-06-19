package types;

import java.util.List;

public class ArrayType extends Type {

	private List<Integer> dimensions;
	private StructType complex;

	public ArrayType(Types type, List<Integer> dimensions) {
		super(type);
		this.dimensions = dimensions;
		this.complex = null;
	}

	public ArrayType(StructType complex, List<Integer> dimensions) {
		super(null);
		this.dimensions = dimensions;
		this.complex = complex;
	}

	public int getSize() {
		/* Just applying the formula */
		int size = 1;
		for (int i = 0; i < this.dimensions.size(); i++) {
			size *= dimensions.get(i);
		}
		/* Is a normal type */
		if (complex != null) {
			size*=complex.getSize();
		}
		return size;
	}

	public List<Integer> getDimensions() {
		return this.dimensions;
	}

	public StructType getComplex() {
		return this.complex;
	}

	public boolean equals(Type other) {
		if (other instanceof ArrayType) {
			ArrayType aux = (ArrayType) other;
			if ((other.getType() == this.getType()) && (aux.getSize() == this.getSize())) {
				for (int i = 0; i < dimensions.size(); i++) {
					if (dimensions.get(i) != aux.getDimensions().get(i)) {
						return false;
					}
				}
			} else {
				return false;
			}
			if (other.getType() == null && this.getType() == null) {
				if (!(aux.getComplex().equals(complex))) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}
}
