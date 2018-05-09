package types;

import java.util.List;

public class ArrayType extends Type {

	private List<Integer> dimensions;

	public ArrayType(Types type, List<Integer> dimensions) {
		super(type);
		this.dimensions = dimensions;
	}

}
