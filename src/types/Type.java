package types;

public class Type {
	protected Types type;

	/* When type is null is a struct type */
	public Type(Types type) {
		this.type = type;
	}

	public Types getType() {
		return this.type;
	}

	/* Checks if two types are equal, warning, it may not commute */
	public boolean equals(Type other) {
		/*
		 * Since getType returns an enumerate, it doesn't matter if we don't use equals
		 * instead of ==, in fact == is null-safe, so better for our purposes
		 */
		return other.getType() == this.type;
	}

	/* In our P-Machine version, a simple type always is 1-sized */
	public int getSize() {
		return 1;
	}
}
