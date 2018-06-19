package statements;

import java.io.BufferedWriter;

import expressions.ArrayIdentifier;
import expressions.Expression;
import expressions.Identifier;
import kaskell.SymbolTable;
import types.Type;
import types.Types;

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

	@Override
	public void generateCode(BufferedWriter bw) throws Exception {
		identifier.generateCode(bw);
		expression.generateCode(bw);
		String typeModifier;
		if (expression.getType().equals(new Type(Types.INTEGER))) {
			typeModifier = "i";
		} else {
			typeModifier = "b";
		}
		bw.write("sto " + typeModifier + "\n");
	}
}
