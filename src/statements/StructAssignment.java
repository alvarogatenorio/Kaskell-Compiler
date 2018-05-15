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
		
		if(!this.member.getType().equals(this.expression.getType())) {
			System.err.println("TYPE ERROR: in line " + (this.member.getRow() + 1) + " column "
					+ (this.member.getColumn() + 1)
					+ " fatal error the expression isn't typed as the struct member in this assignment!");
			return false;
		}
		
		return this.member.checkType() && this.expression.checkType();
	}

	/* Checks the expression and the identifiers (order matters) */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return this.expression.checkIdentifiers(symbolTable) && this.member.checkIdentifiers(symbolTable);
	}
}
