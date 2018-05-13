package statements;

import expressions.Expression;
import expressions.StructMember;
import kaskell.SymbolTable;

public class StructAssignment extends Assignment {
	private StructMember member;

	public StructAssignment(StructMember member, Expression expression) {
		super(null, expression);
		this.member = member;
	}

	public boolean checkType() {
		return this.member.checkType() && this.expression.checkType()
				&& (this.member.getType().equals(this.member.getType()));
	}

	/* Checks the expression and the identifiers (order matters) */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return this.expression.checkIdentifiers(symbolTable) && this.member.checkIdentifiers(symbolTable);
	}
}
