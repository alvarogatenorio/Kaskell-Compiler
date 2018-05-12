package statements;

import expressions.Expression;
import kaskell.SymbolTable;
import types.Type;
import types.Types;

public class ForTuple {
	/* In fact we only allow assignments or mixed */
	private BasicStatement initial;
	protected Expression condition;
	/* In fact we only allow assignments or expressions */
	protected BasicStatement loopEpilogue;

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
		return initial.checkType() && condition.checkType() && (condition.getType().equals(new Type(Types.BOOLEAN)))
				&& loopEpilogue.checkType();
	}

	/* Checks the initial, the condition and the loop epilogue */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = initial.checkIdentifiers(symbolTable);
		wellIdentified = wellIdentified && condition.checkIdentifiers(symbolTable);
		wellIdentified = wellIdentified && loopEpilogue.checkIdentifiers(symbolTable);
		return wellIdentified;
	}
}
