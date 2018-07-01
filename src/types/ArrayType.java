package types;

import java.util.List;

/* ----- Invariant: If the base type is simple, complex is always null,
 * otherwise, the "types" attribute is always null -----*/
public class ArrayType extends Type {

	private List<Integer> dimensions;
	private StructType complex;

	public ArrayType(Types type, List<Integer> dimensions) {
		super(type);
		this.dimensions = dimensions;
		this.complex = null; // love invariant
	}

	public ArrayType(StructType complex, List<Integer> dimensions) {
		super(null); // love invariant
		this.dimensions = dimensions;
		this.complex = complex;
	}

	public int getSize() {
		/* Just applying the formula */
		int size = 1;
		for (int i = 0; i < this.dimensions.size(); i++) {
			size *= dimensions.get(i);
		}
		/* Is not a normal type */
		if (complex != null) {
			size *= complex.getSize();
		}
		return size;
	}

	public List<Integer> getDimensions() {
		return this.dimensions;
	}

	public StructType getComplex() {
		return this.complex;
	}

	/* ----- Precondition: Other is never null ----- */
	public boolean equals(Type other) {
		/* If the other is not an array, we can go home */
		if (other instanceof ArrayType) {
			ArrayType aux = (ArrayType) other;
			/*
			 * We use == instead of equals because they are enumerates, so it doesn't
			 * matter, but, == is null-safe, which is useful when we have null-based
			 * invariants like in this case.
			 * 
			 * If the base type is not the same or the dimensions don't match, we can go
			 * home
			 */
			if ((aux.getType() == this.getType()) && (aux.getSize() == this.getSize())) {
				for (int i = 0; i < dimensions.size(); i++) {
					int a = dimensions.get(i);
					int b = aux.getDimensions().get(i);
					if (a != b) {
						return false;
					}
				}
			} else {
				return false;
			}

			/*
			 * If both are complex-typed arrays, we check the equality of both complex types
			 */
			if (aux.getType() == null && this.getType() == null) {
				if (!(aux.getComplex().equals(complex))) {
					return false;
				}
			}
		} else {
			return false;
		}
		/* If survived, we are good to go */
		return true;
	}
}
