package statements;

import expressions.Expression;

public class ForTuple {
	private BasicStatement initial;
	protected Expression condition;
	protected Assignment loopAssignment;

	public ForTuple(Assignment initialAssignment, Expression condition, Assignment loopAssignment) {
		this.initial = initialAssignment;
		this.condition = condition;
		this.loopAssignment = loopAssignment;
	}

	public ForTuple(Mixed initialMixed, Expression condition, Assignment loopAssignent) {
		this.initial = initialMixed;
		this.condition = condition;
		this.loopAssignment = loopAssignment;
	}
}
