package expressions;

import java.util.List;

public class ArrayIdentifier extends Identifier {
	private List<Expression> coordinates;

	public ArrayIdentifier(String s, List<Expression> coordinates) {
		super(s);
		this.coordinates = coordinates;
	}

}
