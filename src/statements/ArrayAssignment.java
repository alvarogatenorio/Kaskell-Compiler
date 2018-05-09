package statements;

import java.util.List;

import expressions.Expression;
import expressions.Identifier;

public class ArrayAssignment extends Assignment {
	private List<Integer> coordinates;

	public ArrayAssignment(Identifier identifier, List<Integer> coordinates, Expression expression) {
		super(identifier, expression);
		this.coordinates = coordinates;
	}
}
