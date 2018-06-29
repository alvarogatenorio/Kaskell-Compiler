package statements;

import expressions.Expression;
import expressions.StructMember;
import kaskell.Instructions;
import kaskell.SymbolTable;

public class StructAssignment extends Assignment {
	private StructMember member;

	public StructAssignment(StructMember member, Expression expression) {
		super(null, expression);
		this.member = member;
	}

	public boolean checkType() {

		if (!this.member.getSimpleType().equals(this.expression.getType())) {
			System.err.println(
					"TYPE ERROR: in line " + (this.member.getRow() + 1) + " column " + (this.member.getColumn() + 1)
							+ " fatal error the expression isn't typed as the struct member in this assignment!");
			return false;
		}

		return this.member.checkType() && this.expression.checkType();
	}

	/* Checks the expression and the identifiers (order matters) */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return this.expression.checkIdentifiers(symbolTable) && this.member.checkIdentifiers(symbolTable);
	}

	public void generateCode(Instructions instructions) {
		instructions.addComment("{ StructType assignment }\n");
		member.generateCode(instructions);
		/* Just removing the final IND instruction */
		instructions.remove(instructions.size() - 1);
		expression.generateCode(instructions);
		instructions.add("sto;\n");
		instructions.addComment("{ End of StructType assignment }\n");
	}
	
	public StructMember getMember() {
		return this.member;
	}
}
