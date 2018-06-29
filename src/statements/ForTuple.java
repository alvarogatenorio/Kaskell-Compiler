package statements;

import expressions.Expression;
import kaskell.SymbolTable;
import types.Type;
import types.Types;

public class ForTuple {
	/* In fact we only allow assignments or mixed */
	private BasicStatement initial;
	private Expression condition;
	/* In fact we only allow assignments or expressions */
	private BasicStatement loopEpilogue;

	/*
	 * If the above restrictions are not fulfilled a syntax error must have occurred
	 */
	public ForTuple(BasicStatement initialAssignment, Expression condition, BasicStatement loopAssignment) {
		this.initial = initialAssignment;
		this.condition = condition;
		this.loopEpilogue = loopAssignment;
	}

	/*
	 * Just checks the type of each member of the tuple and checks if the condition
	 * is a boolean expression
	 */
	public boolean checkType() {
		Type conditionType;
		conditionType = condition.getType();
		if ((conditionType == null) || (!conditionType.equals(new Type(Types.BOOLEAN)))) {
			System.err.println("TYPE ERROR: in line " + (this.condition.getRow() + 1) + " column "
					+ (this.condition.getColumn() + 1) + " fatal error the condition is not kool type!");
			return false;
		}

		return condition.checkType() && initial.checkType() && loopEpilogue.checkType();
	}

	/* Checks the initial, the condition and the loop epilogue */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = initial.checkIdentifiers(symbolTable);
		wellIdentified = wellIdentified && condition.checkIdentifiers(symbolTable);
		wellIdentified = wellIdentified && loopEpilogue.checkIdentifiers(symbolTable);
		return wellIdentified;
	}

	public BasicStatement getInitial() {
		return initial;
	}

	public BasicStatement getCondition() {
		return condition;
	}

	public BasicStatement getLoopEpilogue() {
		return loopEpilogue;
	}
}
