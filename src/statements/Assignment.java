package statements;

import expressions.ArrayIdentifier;
import expressions.Expression;
import expressions.Identifier;
import kaskell.Instructions;
import kaskell.SymbolTable;
import types.Type;

public class Assignment implements BasicStatement {
	protected Expression expression;
	protected Identifier identifier;

	public Assignment(Identifier identifier, Expression expression) {
		this.identifier = identifier;
		this.expression = expression;
	}

	/*
	 * Checks the type of the expression and then checks if it equals the type of
	 * the identifier
	 */
	public boolean checkType() {
		boolean wellTyped = expression.checkType();
		Type identifierType = identifier.getSimpleType();
		Type expressionType;
		if (expression instanceof ArrayIdentifier) {
			expressionType = ((ArrayIdentifier) (expression)).getSimpleType();
		} else {
			expressionType = expression.getType();
		}
		if (identifierType == null) {
			return false;
		}
		if (!identifierType.equals(expressionType)) {
			System.err.println("TYPE ERROR: in line " + (this.identifier.getRow() + 1) + " column "
					+ (this.identifier.getColumn() + 1)
					+ " the right type of the assignment does not match with the left type of assigment!");
			return false;
		}
		return wellTyped;
	}

	/* Checks the expression and the identifier (order matters) */
	public boolean checkIdentifiers(SymbolTable symbolTable) {
		return expression.checkIdentifiers(symbolTable) && identifier.checkIdentifiers(symbolTable);
	}

	/* Generates code for a simple assignment */
	public void generateCode(Instructions instructions) {
		instructions.addComment("{ Simple assignment }\n");
		identifier.generateCode(instructions);
		/* Just removing the final IND instruction */
		instructions.remove(instructions.size() - 1);
		expression.generateCode(instructions);
		instructions.add("sto;\n");
		instructions.addComment("{ End of simple assignment }\n");
	}
	
	public Expression getExpression() {
		return this.expression;
	}
	
	public Identifier getIdentifier() {
		return this.identifier;
	}
}
