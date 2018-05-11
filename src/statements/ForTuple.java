package statements;

import expressions.Expression;
import kaskell.SymbolTable;
import types.Type;
import types.Types;

public class ForTuple {
	private BasicStatement initial;
	protected Expression condition;
	protected Assignment loopAssignment;

	public ForTuple(Assignment initialAssignment, Expression condition, Assignment loopAssignment) {
		this.initial = initialAssignment;
		this.condition = condition;
		this.loopAssignment = loopAssignment;
	}

	public ForTuple(Mixed initialMixed, Expression condition, Assignment loopAssignment) {
		this.initial = initialMixed;
		this.condition = condition;
		this.loopAssignment = loopAssignment;
	}

	public boolean checkType() {
		return initial.checkType() && condition.checkType() && (condition.getType() == new Type(Types.BOOLEAN))
				&& loopAssignment.checkType();
	}

	public boolean checkIdentifiers(SymbolTable symbolTable) {
		boolean wellIdentified = initial.checkIdentifiers(symbolTable);
		wellIdentified = wellIdentified && condition.checkIdentifiers(symbolTable);
		wellIdentified = wellIdentified && loopAssignment.checkIdentifiers(symbolTable);
		return wellIdentified;
	}
}
