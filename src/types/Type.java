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

	/* Checks if two types are equal */
	public boolean equals(Type other) {
		return other.getType().equals(this.type);
	}
	
	public int getSize() {
		return 1;
	}
}
